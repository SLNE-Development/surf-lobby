package dev.slne.surf.surfserverselector.velocity.queue;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import dev.slne.surf.surfserverselector.api.queue.ServerQueue;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class ServerQueueImpl implements ServerQueue {

  private final PriorityQueue<UUID> queue;

  public ServerQueueImpl() {
    queue = new PriorityQueue<>((uuid1, uuid2) -> {
      final ServerSelectorPlayer player1 = SurfServerSelectorApi.getPlayer(uuid1);
      final ServerSelectorPlayer player2 = SurfServerSelectorApi.getPlayer(uuid2);

      return Integer.compare(player1.getPriority(), player2.getPriority());
    });
  }

  @Override
  public int getQueuePosition(@NotNull ServerSelectorPlayer player) {
    final UUID uuid = checkNotNull(player, "player").getUuid();
    final Object[] es = queue.toArray();

    for (int i = 0, n = queue.size(); i < n; i++) {
      if (uuid.equals(es[i])) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public void addToQueue(@NotNull ServerSelectorPlayer player) {
  }

  @Override
  public void removeFromQueue(@NotNull ServerSelectorPlayer player) {
    queue.remove(checkNotNull(player, "player").getUuid());
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
