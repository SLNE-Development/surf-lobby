package dev.slne.surf.lobby.velocity.player;

import com.velocitypowered.api.proxy.ConnectionRequestBuilder;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.api.queue.ServerQueue;
import dev.slne.surf.lobby.core.message.Messages;
import dev.slne.surf.lobby.core.permissions.Permissions;
import dev.slne.surf.lobby.core.player.CoreLobbyPlayer;
import dev.slne.surf.lobby.velocity.VelocityMain;
import dev.slne.surf.lobby.velocity.sync.SyncValue;
import dev.slne.surf.lobby.velocity.util.LobbyUtil;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

/**
 * Velocity-specific implementation of {@link CoreLobbyPlayer}, handling player actions such as
 * changing servers and sending messages within the Velocity proxy environment.
 */
public final class VelocityLobbyPlayer extends CoreLobbyPlayer {

  private static final ComponentLogger LOGGER = ComponentLogger.logger("Lobby");

  private boolean isChangingServer = false;

  /**
   * Constructs a new VelocityLobbyPlayer with the specified UUID.
   *
   * @param uuid the UUID of the player.
   */
  public VelocityLobbyPlayer(UUID uuid) {
    super(uuid);
  }

  @Override
  public void changeServer(@Nullable String serverName, boolean sendFeedback,
      boolean fallbackToLobbyWithLowestPlayerCount) {
    if (isChangingServer) {
      Messages.ALREADY_CHANGING_SERVER.send(this);
      return;
    }

    final Optional<Player> player = getPlayer();

    if (serverName != null && player.isPresent()) {
      final Optional<ServerConnection> serverConnection = player.get().getCurrentServer();

      if (serverConnection.isPresent() && serverConnection.get().getServerInfo().getName()
          .equals(serverName)) {
        Messages.ALREADY_CONNECTED_TO_SERVER.send(this, Component.text(serverName));
        return;
      }
    }

    isChangingServer = true;

    final CompletableFuture<String> serverNameFuture = serverName == null
        ? LobbyUtil.getLobbyServerWithLowestPlayerCount()
        .thenApply(server -> server.getServerInfo().getName())
        : CompletableFuture.completedFuture(serverName);

    serverNameFuture.thenAcceptAsync(resolvedServerName -> {
      final boolean isRequestedServerLobby = LobbyUtil.isLobbyServer(resolvedServerName);

      final CompletableFuture<ChangeServerResult> futureResult = isRequestedServerLobby
          ? changeToLobbyServer(resolvedServerName, fallbackToLobbyWithLowestPlayerCount)
          : doActualChangeServer(resolvedServerName);

      futureResult.thenAcceptAsync(result -> {
        isChangingServer = false;

        if (sendFeedback) {
          switch (result) {
            // @formatter:off
            case SUCCESS -> Messages.CHANGED_TO_SERVER.send(this, Component.text(resolvedServerName));
            case SUCCESS_LOBBY_SERVER -> Messages.CHANGED_TO_LOBBY_SERVER.send(this, Component.text(resolvedServerName));
            case SUCCESS_WITH_BYPASS_PERMISSION -> Messages.CHANGED_TO_FULL_SERVER_WITH_BYPASS_PERMISSION.send(this, Component.text(resolvedServerName));
            case SUCCESS_BUT_OTHER_LOBBY_SERVER_AS_REQUESTED -> Messages.CHANGED_TO_OTHER_LOBBY_AS_REQUESTED.send(this, Component.text(resolvedServerName));
            case QUEUE -> Messages.JOINED_QUEUE.send(this, Component.text(resolvedServerName));
            case ALREADY_IN_QUEUE -> Messages.ALREADY_IN_QUEUE.send(this, Component.text(resolvedServerName));
            case LOBBY_SERVER_NOT_AVAILABLE -> Messages.LOBBY_SERVER_NOT_AVAILABLE.send(this, Component.text(resolvedServerName));
            case LOBBY_SERVER_FULL -> Messages.LOBBY_SERVER_FULL.send(this, Component.text(resolvedServerName));
            case COULD_NOT_ESTABLISH_CONNECTION -> Messages.COULD_NOT_ESTABLISH_CONNECTION_WITH_SERVER.send(this, Component.text(resolvedServerName));
            case ERROR -> Messages.ERROR_CHANGING_SERVER.send(this, Component.text(resolvedServerName));
            case PLAYER_NOT_ONLINE -> { /* Do nothing */ }
            // @formatter:on
          }
        }
      }).exceptionally(throwable -> {
        isChangingServer = false;
        Messages.ERROR_CHANGING_SERVER.send(this, Component.text(resolvedServerName));
        LOGGER.error("Error changing server for player {}", uuid, throwable);
        return null;
      });
    }).exceptionally(throwable -> {
      isChangingServer = false;
      Messages.ERROR_CHANGING_SERVER.send(this,
          Component.text(serverName == null ? "__Lobby__" : serverName));
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
  private CompletableFuture<ChangeServerResult> changeToLobbyServer(String serverName,
      boolean fallbackToLobbyWithLowestPlayerCount) {
    return getPlayer()
        .map(player -> {
          final ProxyServer server = VelocityMain.getInstance().getServer();

          return server.getServer(serverName)
              .map(lobbyServer -> {
                final int playerCount = lobbyServer.getPlayersConnected().size();
                final int maxPlayers = SyncValue.MAX_PLAYER_COUNT.get(serverName);

                if (playerCount >= maxPlayers
                    && !SyncValue.READY_STATE.get(serverName)
                    && !player.hasPermission(
                    Permissions.BYPASS_PLAYER_LIMIT_PERMISSION.getPermission())) {
                  if (fallbackToLobbyWithLowestPlayerCount) {
                    return LobbyUtil.getLobbyServerWithLowestPlayerCount()
                        .thenApplyAsync(player::createConnectionRequest)
                        .thenComposeAsync(ConnectionRequestBuilder::connectWithIndication)
                        .thenApply(
                            success -> success
                                ? ChangeServerResult.SUCCESS_BUT_OTHER_LOBBY_SERVER_AS_REQUESTED
                                : ChangeServerResult.ERROR);
                  } else {
                    return CompletableFuture.completedFuture(ChangeServerResult.LOBBY_SERVER_FULL);
                  }
                }

                return player.createConnectionRequest(lobbyServer).connectWithIndication()
                    .thenApply(
                        success -> success ? ChangeServerResult.SUCCESS : ChangeServerResult.ERROR);
              }).orElseGet(() -> {
                // If the lobby server is not available, fallback to another lobby server
                if (fallbackToLobbyWithLowestPlayerCount) {
                  LobbyUtil.getLobbyServerWithLowestPlayerCount()
                      .thenApplyAsync(player::createConnectionRequest)
                      .thenComposeAsync(ConnectionRequestBuilder::connectWithIndication)
                      .thenApply(
                          success -> success
                              ? ChangeServerResult.SUCCESS_BUT_OTHER_LOBBY_SERVER_AS_REQUESTED
                              : ChangeServerResult.ERROR);
                }
                return CompletableFuture.completedFuture(
                    ChangeServerResult.LOBBY_SERVER_NOT_AVAILABLE);
              });
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
          .map(requestedServer -> {
//            final int playerCount = requestedServer.getPlayersConnected().size();
//            final int maxPlayers = SyncValue.MAX_PLAYER_COUNT.get(serverName);
            final Optional<ServerQueue> currentQueue = LobbyApi.getInstance()
                .getQueueRegistry().getCurrentQueue(this);

            if (currentQueue.isPresent()) {
              final String currentQueueServerName = currentQueue.get().getServerName();

              if (currentQueueServerName.equals(serverName)) {
                return CompletableFuture.completedFuture(ChangeServerResult.ALREADY_IN_QUEUE);
              }

              currentQueue.get().removeFromQueue(uuid);
            }

            if (!SyncValue.READY_STATE.get(serverName)) {
              if (player.hasPermission(Permissions.BYPASS_QUEUE_PERMISSION.getPermission())) {
                return player.createConnectionRequest(requestedServer)
                    .connectWithIndication()
                    .thenApply(
                        success -> success ? ChangeServerResult.SUCCESS_WITH_BYPASS_PERMISSION
                            : ChangeServerResult.ERROR);
              } else {
                LobbyApi.getInstance().getQueueRegistry().getQueue(serverName)
                    .addToQueue(this);
                return CompletableFuture.completedFuture(ChangeServerResult.QUEUE);
              }
            }

//            if (maxPlayers == -1 || playerCount >= maxPlayers) {
              if (player.hasPermission(Permissions.BYPASS_QUEUE_PERMISSION.getPermission())) {
                return player.createConnectionRequest(requestedServer)
                    .connectWithIndication()
                    .thenApply(
                        success -> success ? ChangeServerResult.SUCCESS_WITH_BYPASS_PERMISSION
                            : ChangeServerResult.ERROR);
              }

              LobbyApi.getInstance().getQueueRegistry().getQueue(serverName)
                  .addToQueue(this);
              return CompletableFuture.completedFuture(ChangeServerResult.QUEUE);
//            }

//            return player.createConnectionRequest(requestedServer)
//                .connectWithIndication()
//                .thenApply(
//                    success -> success ? ChangeServerResult.SUCCESS : ChangeServerResult.ERROR);
          }).orElse(
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
    LOBBY_SERVER_FULL,
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
