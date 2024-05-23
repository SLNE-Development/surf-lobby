package dev.slne.surf.surfserverselector.velocity.queue;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableSet;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import dev.slne.surf.surfserverselector.api.queue.ServerQueue;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.sync.SyncValue;
import dev.slne.surf.surfserverselector.velocity.sync.SyncValue.PlayerCountChangeListener;
import dev.slne.surf.surfserverselector.velocity.sync.SyncValue.ReadyStateChangeListener;
import dev.slne.surf.surfserverselector.velocity.util.LobbyUtil;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ServerQueueImpl implements ServerQueue, PlayerCountChangeListener,
    ReadyStateChangeListener {

  private final PriorityBlockingQueue<UUID> queue;
  private final String serverName;

  @Contract(pure = true)
  public ServerQueueImpl(String serverName) {
    this.serverName = serverName;

    queue = new PriorityBlockingQueue<>(50, (uuid1, uuid2) -> {
      final ServerSelectorPlayer player1 = SurfServerSelectorApi.getPlayer(uuid1);
      final ServerSelectorPlayer player2 = SurfServerSelectorApi.getPlayer(uuid2);

      return Integer.compare(player2.getPriority(), player1.getPriority());
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
    queue.add(checkNotNull(uuid, "uuid"));
  }

  @Override
  public void removeFromQueue(@NotNull UUID uuid) {
    queue.remove(checkNotNull(uuid, "uuid"));
  }

  @Override
  public boolean isInQueue(UUID uuid) {
    return queue.contains(uuid);
  }

  @Override
  public Set<UUID> getPlayersInQueue() {
    return ImmutableSet.copyOf(queue);
  }

  @Override
  public void clearQueue() {
    queue.clear();
  }

  @Override
  public Optional<UUID> poll() {
    return Optional.ofNullable(queue.poll());
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
}
