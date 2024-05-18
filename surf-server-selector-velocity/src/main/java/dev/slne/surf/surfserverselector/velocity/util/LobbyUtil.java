package dev.slne.surf.surfserverselector.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing.Players;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import dev.slne.surf.surfserverselector.api.queue.ServerQueue;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
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
  private static final Map<String, ReentrantLock> serverLocks = new ConcurrentHashMap<>();

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
      final ServerQueue queue = SurfServerSelectorApi.getInstance().getQueueRegistry()
          .getQueue(previousServerName);

      if (!queue.hasPlayersInQueue()) {
        return;
      }

      // Acquire lock for the server
      final ReentrantLock serverLock = serverLocks.computeIfAbsent(previousServerName,
          k -> new ReentrantLock());

      // Try to acquire the lock
      if (serverLock.tryLock()) {
        try {
          previousServer.ping()
              .thenCompose(ping -> {

                // simulate slow server ping
                return CompletableFuture.supplyAsync(() -> {
                  try {
                    LOGGER.info("Simulating slow server ping for server {}", previousServerName);
                    Thread.sleep(10000);
                    LOGGER.info("Server ping completed for server {}", previousServerName);
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                  }
                  return ping;
                });
              })
              .thenAccept(serverPing -> {
                final int playerCount = serverPing.getPlayers().map(Players::getOnline).orElse(0);
                final int maxPlayers = serverPing.getPlayers().map(Players::getMax).orElse(0);

                if (playerCount < maxPlayers) {
                  queue.poll().ifPresent(uuid -> {
                    final ServerSelectorPlayer player = SurfServerSelectorApi.getPlayer(uuid);
                    player.changeServer(previousServerName, true);
                  });
                } else {
                  LOGGER.info("Server {} is full. No player will be transferred from the queue.",
                      previousServerName);
                }
              }).exceptionally(throwable -> {
                LOGGER.error("Failed to ping server {}", previousServerName, throwable);
                return null;
              }).join(); // Ensure the ping process completes before releasing the lock
        } finally {
          // Release lock for the server
          serverLock.unlock();
        }
      } else {
        // Could not acquire the lock, handle the case if necessary
        LOGGER.info("Could not acquire lock for server {}. Skipping transfer from queue.",
            previousServerName);
      }
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
