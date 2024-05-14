package dev.slne.surf.surfserverselector.velocity.player;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing.Players;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import dev.slne.surf.surfserverselector.core.player.CoreServerSelectorPlayer;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.util.LobbyUtil;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.Nullable;

public final class VelocityServerSelectorPlayer extends CoreServerSelectorPlayer {

  private boolean isChangingServer = false;

  public VelocityServerSelectorPlayer(UUID uuid) {
    super(uuid);
  }

  @Override
  public void changeServer(@Nullable String serverName) {
    if (isChangingServer) {
      return;
    }

    isChangingServer = true;

    serverName =
        serverName == null ? LobbyUtil.getLobbyServerWithLowestPlayerCount().getServerInfo()
            .getName() : serverName;

    final boolean isRequestedServerLobby = LobbyUtil.isLobbyServer(serverName);

    if (isRequestedServerLobby) {
      changeToLobbyServer(serverName).thenRun(() -> isChangingServer = false);
    } else {
      doActualChangeServer(serverName).thenRun(() -> isChangingServer = false);
    }
  }

  private CompletableFuture<?> changeToLobbyServer(String serverName) {
    return getPlayer()
        .map(player -> {
          final ProxyServer server = VelocityMain.getInstance().getServer();

          return server.getServer(serverName)
              .map(lobbyServer -> lobbyServer.ping()
                  .thenApplyAsync(serverPing -> {
                    final Players players = serverPing.getPlayers()
                        .orElseThrow(() -> new IllegalStateException("No players in server ping"));

                    final int playerCount = lobbyServer.getPlayersConnected().size();
                    final int maxPlayers = players.getMax();

                    if (playerCount >= maxPlayers) {
                      return player.createConnectionRequest(
                          LobbyUtil.getLobbyServerWithLowestPlayerCount()).connectWithIndication();
                    }

                    return player.createConnectionRequest(lobbyServer).connectWithIndication();
                  }))
              .orElse(CompletableFuture.completedFuture(null));
        })
        .orElse(CompletableFuture.completedFuture(null));
  }

  private CompletableFuture<?> doActualChangeServer(String serverName) {
    return getPlayer().map(player -> {
      final ProxyServer server = VelocityMain.getInstance().getServer();

      return server.getServer(serverName)
          .map(requestedServer -> requestedServer.ping()
              .thenApplyAsync(serverPing -> {
                final Players players = serverPing.getPlayers()
                    .orElseThrow(() -> new IllegalStateException("No players in server ping"));

                final int playerCount = requestedServer.getPlayersConnected().size();
                final int maxPlayers = players.getMax();

                if (playerCount >= maxPlayers) {
                  if (player.hasPermission(Permissions.BYPASS_QUEUE_PERMISSION.getPermission())) {
                    return player.createConnectionRequest(requestedServer).connectWithIndication();
                  }

                  SurfServerSelectorApi.getInstance().getQueueRegistry().getQueue(serverName)
                      .addToQueue(this);
                  return CompletableFuture.completedFuture(null);
                }

                return player.createConnectionRequest(requestedServer).connectWithIndication();
              })).orElse(CompletableFuture.completedFuture(null));
    }).orElse(CompletableFuture.completedFuture(null));
  }

  @Override
  public Optional<Player> getPlayer() {
    return VelocityMain.getInstance().getServer().getPlayer(uuid);
  }
}
