package dev.slne.surf.lobby.velocity.queue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.api.player.LobbyPlayer;
import dev.slne.surf.lobby.api.queue.ServerQueue;
import dev.slne.surf.lobby.velocity.VelocityMain;
import dev.slne.surf.lobby.velocity.sync.SyncValue;
import dev.slne.surf.lobby.velocity.sync.SyncValue.PlayerCountChangeListener;
import dev.slne.surf.lobby.velocity.sync.SyncValue.ReadyStateChangeListener;
import dev.slne.surf.lobby.velocity.util.LobbyUtil;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ServerQueueImpl implements ServerQueue, PlayerCountChangeListener,
    ReadyStateChangeListener {

  private final PriorityBlockingQueue<QueueEntry> queue;
  private final String serverName;

  @Contract(pure = true)
  public ServerQueueImpl(String serverName) {
    this.serverName = serverName;

    queue = new PriorityBlockingQueue<>(50, (uuid1, uuid2) -> {
      final LobbyPlayer player1 = LobbyApi.getPlayer(uuid1.uuid());
      final LobbyPlayer player2 = LobbyApi.getPlayer(uuid2.uuid());

      int priorityCompare = Integer.compare(player1.getPriority(), player2.getPriority());
      int timeCompare = Long.compare(uuid1.timestamp(), uuid2.timestamp());

      return (priorityCompare != 0 ? priorityCompare : timeCompare);
    });

    SyncValue.MAX_PLAYER_COUNT.subscribe(this);
    SyncValue.READY_STATE.subscribe(this);
  }

  @Override
  public String getServerName() {
    return serverName;
  }

  @Override
  public int getQueuePosition(@NotNull UUID uuid) {
    checkNotNull(uuid, "uuid");

    final Object[] es = queue.toArray();

    for (int i = 0, n = queue.size(); i < n; i++) {
      if (uuid.equals(es[i])) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public void addToQueue(@NotNull UUID uuid) {
    queue.add(new QueueEntry(checkNotNull(uuid, "uuid")));
  }

  @Override
  public void removeFromQueue(@NotNull UUID uuid) {
    queue.removeIf(entry -> entry.uuid().equals(uuid));
  }

  @Override
  public boolean isInQueue(UUID uuid) {
    return queue.stream().anyMatch(entry -> entry.uuid().equals(uuid));
  }

  @Override
  public Set<UUID> getPlayersInQueue() {
    return ImmutableSet.copyOf(queue.stream().map(QueueEntry::uuid).iterator());
  }

  @Override
  public void clearQueue() {
    queue.clear();
  }

  @Override
  public Optional<UUID> poll() {
    return Optional.ofNullable(queue.poll()).map(QueueEntry::uuid);
  }

  @Override
  public boolean hasPlayersInQueue() {
    return !queue.isEmpty();
  }

  @Override
  public RegisteredServer getServer() {
    return VelocityMain.getInstance().getServer().getServer(serverName)
        .orElseThrow(() -> new IllegalStateException("Server not found: " + serverName));
  }

  @Override
  public void onPlayerCountChange(String serverName, int previousCount, int newCount) {
    if (!serverName.equals(this.serverName)) {
      return;
    }

    if (previousCount > newCount) {
      return;
    }

    final int toRemove = newCount - previousCount;

    for (int i = 0; i < toRemove; i++) {
      LobbyUtil.transferPlayerFromQueue(this);
    }
  }

  @Override
  public void onReadyStateChange(String serverName, boolean oldReadyState, boolean newReadyState) {
    if (!serverName.equals(this.serverName)) {
      return;
    }

    if (newReadyState) {
      for (int i = 0; i < SyncValue.MAX_PLAYER_COUNT.get(serverName); i++) {
        LobbyUtil.transferPlayerFromQueue(this);
      }
    }
  }

  private record QueueEntry(UUID uuid, long timestamp) {
    private QueueEntry(UUID uuid) {
      this(uuid, System.currentTimeMillis());
    }
  }
}
