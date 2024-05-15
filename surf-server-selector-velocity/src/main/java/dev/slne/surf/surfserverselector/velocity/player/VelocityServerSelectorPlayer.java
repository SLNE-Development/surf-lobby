package dev.slne.surf.surfserverselector.velocity.player;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing.Players;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.api.queue.ServerQueue;
import dev.slne.surf.surfserverselector.core.message.Messages;
import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import dev.slne.surf.surfserverselector.core.player.CoreServerSelectorPlayer;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.util.LobbyUtil;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Velocity-specific implementation of {@link CoreServerSelectorPlayer}, handling player actions
 * such as changing servers and sending messages within the Velocity proxy environment.
 */
public final class VelocityServerSelectorPlayer extends CoreServerSelectorPlayer {

  private static final ComponentLogger LOGGER = ComponentLogger.logger("ServerSelector");

  private boolean isChangingServer = false;

  /**
   * Constructs a new VelocityServerSelectorPlayer with the specified UUID.
   *
   * @param uuid the UUID of the player.
   */
  public VelocityServerSelectorPlayer(UUID uuid) {
    super(uuid);
  }


  @Override
  public void changeServer(@Nullable String serverName, boolean sendFeedback) {
    if (isChangingServer) {
      Messages.ALREADY_CHANGING_SERVER.send(this);
      return;
    }

    isChangingServer = true;

    serverName =
        serverName == null ? LobbyUtil.getLobbyServerWithLowestPlayerCount().getServerInfo()
            .getName() : serverName;

    final boolean isRequestedServerLobby = LobbyUtil.isLobbyServer(serverName);

    final String finalServerName = serverName;
    final CompletableFuture<ChangeServerResult> futureResult = isRequestedServerLobby
        ? changeToLobbyServer(serverName) : doActualChangeServer(serverName);

    futureResult.thenAcceptAsync(result -> {
      isChangingServer = false;

      if (sendFeedback) {
        switch (result) {
          // @formatter:off
          case SUCCESS -> Messages.CHANGED_TO_SERVER.send(this, Component.text(finalServerName));
          case SUCCESS_LOBBY_SERVER -> Messages.CHANGED_TO_LOBBY_SERVER.send(this, Component.text(finalServerName));
          case SUCCESS_WITH_BYPASS_PERMISSION -> Messages.CHANGED_TO_FULL_SERVER_WITH_BYPASS_PERMISSION.send(this, Component.text(finalServerName));
          case SUCCESS_BUT_OTHER_LOBBY_SERVER_AS_REQUESTED -> Messages.CHANGED_TO_OTHER_LOBBY_AS_REQUESTED.send(this, Component.text(finalServerName));
          case QUEUE -> Messages.JOINED_QUEUE.send(this, Component.text(finalServerName));
          case ALREADY_IN_QUEUE -> Messages.ALREADY_IN_QUEUE.send(this, Component.text(finalServerName));
          case LOBBY_SERVER_NOT_AVAILABLE -> Messages.LOBBY_SERVER_NOT_AVAILABLE.send(this, Component.text(finalServerName));
          case COULD_NOT_ESTABLISH_CONNECTION -> Messages.COULD_NOT_ESTABLISH_CONNECTION_WITH_SERVER.send(this, Component.text(finalServerName));
          case ERROR -> Messages.ERROR_CHANGING_SERVER.send(this, Component.text(finalServerName));
          case PLAYER_NOT_ONLINE -> { /* Do nothing */ }
          // @formatter:on
        }
      }
    }).exceptionally(throwable -> {
      isChangingServer = false;
      Messages.ERROR_CHANGING_SERVER.send(this, Component.text(finalServerName));
      LOGGER.error("Error changing server for player {}", uuid, throwable);
      return null;
    });
  }

  /**
   * Attempts to connect the player to a lobby server, handling full server situations by
   * redirecting to another lobby.
   *
   * @param serverName the lobby server to attempt connection to.
   * @return a {@link CompletableFuture} that indicates the result of the attempt.
   */
  private CompletableFuture<ChangeServerResult> changeToLobbyServer(String serverName) {
    return getPlayer()
        .map(player -> {
          final ProxyServer server = VelocityMain.getInstance().getServer();

          return server.getServer(serverName)
              .map(lobbyServer -> lobbyServer.ping()
                  .thenComposeAsync(serverPing -> {
                    final Players players = serverPing.getPlayers()
                        .orElseThrow(() -> new IllegalStateException("No players in server ping"));

                    final int playerCount = lobbyServer.getPlayersConnected().size();
                    final int maxPlayers = players.getMax();

                    if (playerCount >= maxPlayers) {
                      return player.createConnectionRequest(
                              LobbyUtil.getLobbyServerWithLowestPlayerCount()).connectWithIndication()
                          .thenApply(
                              success -> success
                                  ? ChangeServerResult.SUCCESS_BUT_OTHER_LOBBY_SERVER_AS_REQUESTED
                                  : ChangeServerResult.ERROR);
                    }

                    return player.createConnectionRequest(lobbyServer).connectWithIndication()
                        .thenApply(
                            success -> success ? ChangeServerResult.SUCCESS
                                : ChangeServerResult.ERROR);
                  }))
              .orElse(CompletableFuture.completedFuture(
                  ChangeServerResult.COULD_NOT_ESTABLISH_CONNECTION));
        })
        .orElse(CompletableFuture.completedFuture(ChangeServerResult.PLAYER_NOT_ONLINE));
  }

  /**
   * Attempts to change the server for the player, handling full server scenarios and queue
   * interactions.
   *
   * @param serverName the target server to connect to.
   * @return a {@link CompletableFuture} indicating the result of the server change attempt.
   */
  private CompletableFuture<ChangeServerResult> doActualChangeServer(String serverName) {
    return getPlayer().map(player -> {
      final ProxyServer server = VelocityMain.getInstance().getServer();

      return server.getServer(serverName)
          .map(requestedServer -> requestedServer.ping()
              .thenComposeAsync(serverPing -> {
                final Players players = serverPing.getPlayers()
                    .orElseThrow(() -> new IllegalStateException("No players in server ping"));

                final int playerCount = requestedServer.getPlayersConnected().size();
                final int maxPlayers = players.getMax();
                final Optional<ServerQueue> currentQueue = SurfServerSelectorApi.getInstance()
                    .getQueueRegistry().getCurrentQueue(this);

                if (currentQueue.isPresent()) {
                  final String currentQueueServerName = currentQueue.get().getServerName();

                  if (currentQueueServerName.equals(serverName)) {
                    return CompletableFuture.completedFuture(ChangeServerResult.ALREADY_IN_QUEUE);
                  }

                  currentQueue.get().removeFromQueue(uuid);
                }

                if (playerCount >= maxPlayers) {
                  if (player.hasPermission(Permissions.BYPASS_QUEUE_PERMISSION.getPermission())) {
                    return player.createConnectionRequest(requestedServer)
                        .connectWithIndication()
                        .thenApply(
                            success -> success ? ChangeServerResult.SUCCESS_WITH_BYPASS_PERMISSION
                                : ChangeServerResult.ERROR);
                  }

                  SurfServerSelectorApi.getInstance().getQueueRegistry().getQueue(serverName)
                      .addToQueue(this);
                  return CompletableFuture.completedFuture(ChangeServerResult.QUEUE);
                }

                return player.createConnectionRequest(requestedServer)
                    .connectWithIndication()
                    .thenApply(
                        success -> success ? ChangeServerResult.SUCCESS : ChangeServerResult.ERROR);
              })).orElse(
              CompletableFuture.completedFuture(ChangeServerResult.COULD_NOT_ESTABLISH_CONNECTION));
    }).orElse(CompletableFuture.completedFuture(ChangeServerResult.PLAYER_NOT_ONLINE));
  }

  @Override
  public Optional<Player> getPlayer() {
    return VelocityMain.getInstance().getServer().getPlayer(uuid);
  }

  /**
   * Defines possible results of a server change attempt, indicating various states and outcomes.
   */
  private enum ChangeServerResult {
    SUCCESS,
    SUCCESS_LOBBY_SERVER,
    SUCCESS_WITH_BYPASS_PERMISSION,
    SUCCESS_BUT_OTHER_LOBBY_SERVER_AS_REQUESTED,
    QUEUE,
    ALREADY_IN_QUEUE,
    LOBBY_SERVER_NOT_AVAILABLE,
    COULD_NOT_ESTABLISH_CONNECTION,
    PLAYER_NOT_ONLINE,
    ERROR;

    @Contract(pure = true)
    public boolean isSuccess() {
      return this == SUCCESS || this == SUCCESS_WITH_BYPASS_PERMISSION || this == QUEUE;
    }

    @Contract(pure = true)
    public boolean isFailure() {
      return this == LOBBY_SERVER_NOT_AVAILABLE || this == ERROR;
    }
  }
}
