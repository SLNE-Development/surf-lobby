package dev.slne.surf.lobby.api.queue;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.lobby.api.player.LobbyPlayer;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * <b>Velocity only</b>
 */
@NonExtendable
public interface ServerQueue {

  String getServerName();

  default int getQueuePosition(@NotNull LobbyPlayer player) {
    return getQueuePosition(checkNotNull(player, "player").getUuid());
  }

  int getQueuePosition(@NotNull UUID uuid);

  default void addToQueue(@NotNull LobbyPlayer player) {
    addToQueue(checkNotNull(player, "player").getUuid());
  }

  void addToQueue(@NotNull UUID uuid);

  default void removeFromQueue(@NotNull LobbyPlayer player) {
    removeFromQueue(checkNotNull(player, "player").getUuid());
  }

  void removeFromQueue(@NotNull UUID uuid);

  boolean isInQueue(UUID uuid);

  default boolean isInQueue(LobbyPlayer player) {
    return isInQueue(checkNotNull(player, "player").getUuid());
  }

  @UnmodifiableView
  Set<UUID> getPlayersInQueue();

  void clearQueue();

  Optional<UUID> poll();

  boolean hasPlayersInQueue();

  Object getServer();
}
