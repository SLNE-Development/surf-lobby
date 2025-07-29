package dev.slne.surf.lobby.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.slne.surf.lobby.velocity.VelocityMain;
import dev.slne.surf.lobby.velocity.config.VelocityConfig;
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
 * transferring players from a server.
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

  public static List<RegisteredServer> getAllLobbyServer() {
    final ProxyServer server = VelocityMain.getInstance().getServer();
    final String lobbyServerPrefix = VelocityConfig.get().lobbyServerPrefix();

    return server.getAllServers().stream()
        .filter(registeredServer -> registeredServer.getServerInfo().getName()
            .startsWith(lobbyServerPrefix))
        .toList();
  }

  public static int getCurrentPlayerCount(String serverName) {
    final ProxyServer server = VelocityMain.getInstance().getServer();
    final RegisteredServer serverInstance = server.getServer(serverName)
        .orElseThrow(() -> new IllegalStateException("Server not found: " + serverName));

    return getCurrentPlayerCount(serverInstance);
  }

  public static int getCurrentPlayerCount(RegisteredServer server) {
    return server.getPlayersConnected().size();
  }
}
