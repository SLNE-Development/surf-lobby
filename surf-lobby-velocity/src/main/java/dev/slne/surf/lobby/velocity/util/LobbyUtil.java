package dev.slne.surf.lobby.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.api.player.LobbyPlayer;
import dev.slne.surf.lobby.api.queue.ServerQueue;
import dev.slne.surf.lobby.velocity.VelocityMain;
import dev.slne.surf.lobby.velocity.config.VelocityConfig;
import dev.slne.surf.lobby.velocity.sync.SyncValue;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for handling lobby server operations within a Velocity-based proxy environment.
 * This class provides methods to interact with and manage lobby servers, including retrieving the
 * lobby server with the lowest player count, checking if a server is a lobby server, and
 * transferring players from a server queue.
 */
public final class LobbyUtil {

  private static final ComponentLogger LOGGER = ComponentLogger.logger("LobbyUtil");

  /**
   * Retrieves the lobby server with the lowest number of connected players. The method filters all
   * registered servers by the lobby server prefix defined in the VelocityConfig.
   *
   * @return the {@link RegisteredServer} with the fewest connected players that matches the lobby
   * server prefix.
   * @throws IllegalStateException if no lobby servers are found based on the defined prefix.
   */
  public static CompletableFuture<RegisteredServer> getLobbyServerWithLowestPlayerCount() {
    final ProxyServer server = VelocityMain.getInstance().getServer();
    final String lobbyServerPrefix = VelocityConfig.get().lobbyServerPrefix();

    final List<CompletableFuture<@Nullable RegisteredServer>> pingFutures = server.getAllServers()
        .stream()
        .filter(registeredServer -> registeredServer.getServerInfo().getName()
            .startsWith(lobbyServerPrefix))
        .map(registeredServer -> registeredServer.ping()
            .thenApply(pingResult -> pingResult == null ? null : registeredServer)
            .exceptionally(throwable -> null))
        .toList();

    return CompletableFuture.allOf(pingFutures.toArray(CompletableFuture[]::new))
        .thenApply(v -> pingFutures.stream()
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .min(Comparator.comparingInt(
                registeredServer -> registeredServer.getPlayersConnected().size()))
            .orElseThrow(() -> new IllegalStateException("No online lobby servers found")));
  }

  /**
   * Checks if the given server name represents a lobby server.
   *
   * @param serverName the name of the server to check.
   * @return true if the server name starts with the lobby server prefix, false otherwise.
   */
  public static boolean isLobbyServer(@NotNull String serverName) {
    return serverName.startsWith(VelocityConfig.get().lobbyServerPrefix());
  }

  /**
   * Checks if the given {@link RegisteredServer} is a lobby server.
   *
   * @param previousServer the server to check.
   * @return true if the server is a lobby server, false otherwise.
   */
  public static boolean isLobbyServer(@NotNull RegisteredServer previousServer) {
    return isLobbyServer(previousServer.getServerInfo().getName());
  }

  /**
   * Transfers a player from the queue of a specified server if it is not a lobby server. This
   * method is typically invoked when a player leaves a non-lobby server, allowing the next player
   * in the queue to be transferred to that server.
   *
   * @param previousServer the server from which a player has left.
   */
  public static void transferPlayerFromQueue(RegisteredServer previousServer) {
    final boolean isPreviousServerLobby = isLobbyServer(previousServer);

    if (!isPreviousServerLobby) {
      final String previousServerName = previousServer.getServerInfo().getName();
      final ServerQueue queue = LobbyApi.getInstance().getQueueRegistry()
          .getQueue(previousServerName);

      transferPlayerFromQueue(queue);
    }
  }

  public static void transferPlayerFromQueue(ServerQueue queue) {
    if (!queue.hasPlayersInQueue()) {
      return;
    }

    final RegisteredServer queuedServer = (RegisteredServer) queue.getServer();
    final String queueServerName = queue.getServerName();

    final int playerCount = queuedServer.getPlayersConnected().size();
    final int maxPlayers = SyncValue.MAX_PLAYER_COUNT.get(queueServerName);

    if (playerCount < maxPlayers) {
      queue.poll().ifPresent(uuid -> {
        final LobbyPlayer player = LobbyApi.getPlayer(uuid);
        player.changeServer(queueServerName, true);
      });
    } else {
      LOGGER.info("Server {} is full. No player will be transferred from the queue.",
          queueServerName);
    }
  }

  public static List<RegisteredServer> getAllLobbyServer() {
    final ProxyServer server = VelocityMain.getInstance().getServer();
    final String lobbyServerPrefix = VelocityConfig.get().lobbyServerPrefix();

    return server.getAllServers().stream()
        .filter(registeredServer -> registeredServer.getServerInfo().getName()
            .startsWith(lobbyServerPrefix))
        .toList();
  }
}
