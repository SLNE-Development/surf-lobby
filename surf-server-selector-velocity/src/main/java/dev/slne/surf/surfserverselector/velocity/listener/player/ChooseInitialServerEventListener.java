package dev.slne.surf.surfserverselector.velocity.listener.player;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import dev.slne.surf.surfserverselector.velocity.util.LobbyUtil;
import org.jetbrains.annotations.NotNull;

public final class ChooseInitialServerEventListener {

  @Subscribe
  public void onPlayerChooseInitialServer(@NotNull PlayerChooseInitialServerEvent event) {
    event.setInitialServer(LobbyUtil.getLobbyServerWithLowestPlayerCount());
  }
}
