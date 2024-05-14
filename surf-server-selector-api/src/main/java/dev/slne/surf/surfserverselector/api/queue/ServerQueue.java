package dev.slne.surf.surfserverselector.api.queue;

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

  int getQueuePosition(@NotNull ServerSelectorPlayer player);

  void addToQueue(@NotNull ServerSelectorPlayer player);

  void removeFromQueue(@NotNull ServerSelectorPlayer player);

  void clearQueue();

  Optional<UUID> poll();
}
