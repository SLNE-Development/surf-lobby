package dev.slne.surf.surfserverselector.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;
import java.util.Comparator;

public final class LobbyUtil {

  public static RegisteredServer getLobbyServerWithLowestPlayerCount() {
    final ProxyServer server = VelocityMain.getInstance().getServer();
    final String lobbyServerPrefix = VelocityConfig.get().lobbyServerPrefix();

    return server.getAllServers().stream()
        .filter(registeredServer -> registeredServer.getServerInfo().getName().startsWith(lobbyServerPrefix))
        .min(Comparator.comparingInt(server2 -> server2.getPlayersConnected().size()))
        .orElseThrow(() -> new IllegalStateException("No lobby servers found"));
  }
}
