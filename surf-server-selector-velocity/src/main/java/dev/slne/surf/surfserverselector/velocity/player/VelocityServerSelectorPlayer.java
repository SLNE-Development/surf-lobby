package dev.slne.surf.surfserverselector.velocity.player;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing.Players;
import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import dev.slne.surf.surfserverselector.core.player.CoreServerSelectorPlayer;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.queue.ServerQueueImpl;
import dev.slne.surf.surfserverselector.velocity.queue.ServerQueueRegistry;
import java.util.Optional;
import java.util.UUID;

public final class VelocityServerSelectorPlayer extends CoreServerSelectorPlayer {

  public VelocityServerSelectorPlayer(UUID uuid) {
    super(uuid);
  }

  @Override
  public void changeServer(String serverName) {
    doActualChangeServer(serverName);
  }

  public void doActualChangeServer(String serverName) {
    getPlayer().ifPresent(player -> {
      final ProxyServer server = VelocityMain.getInstance().getServer();
      server.getServer(serverName).ifPresent(requestedServer -> {
        requestedServer.ping().thenAcceptAsync(serverPing -> {
          final Players players = serverPing.getPlayers().orElseThrow(() -> new IllegalStateException("No players in server ping"));

          final int playerCount = requestedServer.getPlayersConnected().size();
          final int maxPlayers = players.getMax();

          if (playerCount >= maxPlayers) {
            if (player.hasPermission(Permissions.BYPASS_QUEUE_PERMISSION.getPermission())) {
              player.createConnectionRequest(requestedServer).fireAndForget();
              return;
            }

            ServerQueueRegistry.INSTANCE.getQueue(serverName).addToQueue(this);
            return;
          }

          player.createConnectionRequest(requestedServer).fireAndForget();
        });
      });
    });
  }

  @Override
  public Optional<Player> getPlayer() {
    return VelocityMain.getInstance().getServer().getPlayer(uuid);
  }
}
