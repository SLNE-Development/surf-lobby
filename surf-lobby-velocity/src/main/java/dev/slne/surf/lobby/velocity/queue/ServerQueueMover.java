package dev.slne.surf.lobby.velocity.queue;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.api.queue.ServerQueue;
import dev.slne.surf.lobby.velocity.sync.SyncValue;
import dev.slne.surf.lobby.velocity.util.LobbyUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ServerQueueMover {

  @Scheduled(fixedRate = 1010L) // 1 player per second
  protected void moveIfFree() {
    for (final ServerQueue queue : LobbyApi.getInstance().getQueueRegistry().getGlobalQueues()) {
      final int playerCount = LobbyUtil.getCurrentPlayerCount((RegisteredServer) queue.getServer());
      final int maxPlayers = SyncValue.MAX_PLAYER_COUNT.get(queue.getServerName());

      if (playerCount < maxPlayers) {
        LobbyUtil.transferPlayerFromQueue(queue);
      }
    }
  }
}
