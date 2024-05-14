package dev.slne.surf.surfserverselector.api.queue;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * <b>Velocity only</b>
 */
public interface ServerQueueRegistry {

  default ServerQueue getQueue(@NotNull String serverName) {
    throw new UnsupportedOperationException("This method is not supported in this implementation.");
  }

  default void removeFromQueue(@NotNull ServerSelectorPlayer player) {
    removeFromQueue(checkNotNull(player, "player").getUuid());
  }

  default void removeFromQueue(@NotNull UUID uuid) {
    throw new UnsupportedOperationException("This method is not supported in this implementation.");
  }

  @Contract(value = " -> new", pure = true)
  @Internal
  static @NotNull ServerQueueRegistry createNoOp() {
    return new ServerQueueRegistry() {
    };
  }
}
