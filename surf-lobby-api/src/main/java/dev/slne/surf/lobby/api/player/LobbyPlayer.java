package dev.slne.surf.lobby.api.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a player involved in server selection processes, encapsulating operations such as
 * server changing and message sending within a server selector context. This interface is marked as
 * non-extendable.
 */
@NonExtendable
public interface LobbyPlayer {

  /**
   * Retrieves the unique identifier of the player.
   *
   * @return the UUID of the player.
   */
  UUID getUuid();

  /**
   * Gets an {@link Optional} that may contain the {@link Audience} instance representing the
   * player.
   *
   * @return an Optional possibly containing the Audience of the player.
   */
  Optional<? extends Audience> getPlayer();

  /**
   * Requests the player to change to a specified server. Optionally sends feedback about the
   * action.
   *
   * @param serverName   the name of the server to change to; if null, defaults to the lobby server
   *                     with the lowest player count.
   * @param sendFeedback whether to send feedback to the player about the server change.
   */
  default void changeServer(@Nullable String serverName, boolean sendFeedback) {
    changeServer(serverName, sendFeedback, true);
  }

  /**
   * Requests the player to change to a specified server. Optionally sends feedback about the
   * action. Optionally falls back to the lobby server with the lowest player count.
   *
   * @param serverName                           the name of the server to change to; if null,
   *                                                                                   defaults to
   *                                             the lobby server with the lowest player
   *                                             count.
   * @param sendFeedback                         whether to send feedback to the player about the
   *                                                                                      server
   *                                             change.
   * @param fallbackToLobbyWithLowestPlayerCount whether to fall back to the lobby server with the
   *                                                                                       lowest
   *                                             player count if the specified lobby server
   *                                             is full.
   */
  void changeServer(@Nullable String serverName, boolean sendFeedback,
      boolean fallbackToLobbyWithLowestPlayerCount);

  /**
   * Transfer to lobby.
   */
  default void transferToLobby() {
    changeServer(null, true);
  }

  /**
   * Retrieves the priority of the player based on server-specific metadata.
   *
   * @return the priority level extracted from the user's metadata, or -1 if not available.
   */
  int getPriority(); // TODO: use luckperms group weight

  /**
   * Gets queue position.
   *
   * @return the queue position
   */
  int getQueuePosition();

  /**
   * Sets queue position.
   *
   * @param position the position
   */
  void setQueuePosition(int position);

  /**
   * Sends a message to the player.
   *
   * @param message the message to send to the player, must not be null.
   */
  default void sendMessage(@NotNull ComponentLike message) {
    this.getPlayer().ifPresent(player -> player.sendMessage(checkNotNull(message, "message")));
  }

  /**
   * Send action bar.
   *
   * @param message the message
   */
  default void sendActionBar(@NotNull ComponentLike message) {
    this.getPlayer().ifPresent(player -> player.sendActionBar(message));
  }
}
