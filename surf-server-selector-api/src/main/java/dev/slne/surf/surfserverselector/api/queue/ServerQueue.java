package dev.slne.surf.surfserverselector.api.queue;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.NotNull;

/**
 * <b>Velocity only</b>
 */
@NonExtendable
public interface ServerQueue {

  default int getQueuePosition(@NotNull ServerSelectorPlayer player) {
    return getQueuePosition(checkNotNull(player, "player").getUuid());
  }

  int getQueuePosition(@NotNull UUID uuid);

  default void addToQueue(@NotNull ServerSelectorPlayer player) {
    addToQueue(checkNotNull(player, "player").getUuid());
  }

  void addToQueue(@NotNull UUID uuid);

  default void removeFromQueue(@NotNull ServerSelectorPlayer player) {
    removeFromQueue(checkNotNull(player, "player").getUuid());
  }

  void removeFromQueue(@NotNull UUID uuid);

  void clearQueue();

  Optional<UUID> poll();
}
