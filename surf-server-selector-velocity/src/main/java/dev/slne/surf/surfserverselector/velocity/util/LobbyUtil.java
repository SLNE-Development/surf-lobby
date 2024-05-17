package dev.slne.surf.surfserverselector.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import dev.slne.surf.surfserverselector.api.queue.ServerQueue;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for handling lobby server operations within a Velocity-based proxy environment.
 * This class provides methods to interact with and manage lobby servers, including retrieving the
 * lobby server with the lowest player count, checking if a server is a lobby server, and
 * transferring players from a server queue.
 */
public final class LobbyUtil {

  /**
   * Retrieves the lobby server with the lowest number of connected players. The method filters all
   * registered servers by the lobby server prefix defined in the VelocityConfig.
   *
   * @return the {@link RegisteredServer} with the fewest connected players that matches the lobby
   * server prefix.
   * @throws IllegalStateException if no lobby servers are found based on the defined prefix.
   */
  public static RegisteredServer getLobbyServerWithLowestPlayerCount() {
    final ProxyServer server = VelocityMain.getInstance().getServer();
    final String lobbyServerPrefix = VelocityConfig.get().lobbyServerPrefix();

    return server.getAllServers().stream()
        .filter(registeredServer -> registeredServer.getServerInfo().getName()
            .startsWith(lobbyServerPrefix))
        .min(Comparator.comparingInt(
            registeredServer -> registeredServer.getPlayersConnected().size()))
        .orElseThrow(() -> new IllegalStateException("No lobby servers found"));
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
      final ServerQueue queue = SurfServerSelectorApi.getInstance().getQueueRegistry()
          .getQueue(previousServerName);

      queue.poll().ifPresent(uuid -> {
        final ServerSelectorPlayer player = SurfServerSelectorApi.getPlayer(uuid);
        player.changeServer(previousServerName, true);
      });
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
