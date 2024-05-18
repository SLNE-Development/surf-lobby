package dev.slne.surf.surfserverselector.velocity.listener.player;

import com.velocitypowered.api.event.Continuation;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import dev.slne.surf.surfserverselector.velocity.util.LobbyUtil;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

public final class ChooseInitialServerEventListener {

  private static final ComponentLogger LOGGER = ComponentLogger.logger("ChooseInitialServerEventListener");

  @Subscribe
  public void onPlayerChooseInitialServer(@NotNull PlayerChooseInitialServerEvent event,
      Continuation continuation) {
    LobbyUtil.getLobbyServerWithLowestPlayerCount().thenAcceptAsync(server -> {
      event.setInitialServer(server);
      continuation.resume();
    }).exceptionally(throwable -> {
      LOGGER.error("Failed to retrieve lobby server with lowest player count", throwable);
      continuation.resumeWithException(throwable);

      return null;
    });
  }
}
