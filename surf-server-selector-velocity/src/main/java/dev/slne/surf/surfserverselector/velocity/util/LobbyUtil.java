package dev.slne.surf.surfserverselector.velocity.util;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;
import java.util.Comparator;
import java.util.Optional;

public final class LobbyUtil {

  public static RegisteredServer getLobbyServerWithLowestPlayerCount() {
    final ProxyServer server = VelocityMain.getInstance().getServer();

    return VelocityConfig.get().lobbyServerNames().stream()
        .map(server::getServer)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .min(Comparator.comparingInt(server2 -> server2.getPlayersConnected().size()))
        .orElseThrow(() -> new IllegalStateException("No lobby servers found"));
  }
}
