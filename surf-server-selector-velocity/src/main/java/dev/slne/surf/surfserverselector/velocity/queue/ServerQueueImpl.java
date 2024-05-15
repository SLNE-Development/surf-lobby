package dev.slne.surf.surfserverselector.velocity.queue;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import dev.slne.surf.surfserverselector.api.queue.ServerQueue;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.PriorityBlockingQueue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ServerQueueImpl implements ServerQueue {

  private final PriorityBlockingQueue<UUID> queue;
  private final String serverName;

  @Contract(pure = true)
  public ServerQueueImpl(String serverName) {
    this.serverName = serverName;

    queue = new PriorityBlockingQueue<>(50, (uuid1, uuid2) -> {
      final ServerSelectorPlayer player1 = SurfServerSelectorApi.getPlayer(uuid1);
      final ServerSelectorPlayer player2 = SurfServerSelectorApi.getPlayer(uuid2);

      return Integer.compare(player1.getPriority(), player2.getPriority());
    });
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
  public void clearQueue() {
    queue.clear();
  }

  @Override
  public Optional<UUID> poll() {
    return Optional.ofNullable(queue.poll());
  }
}
