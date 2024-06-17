package dev.slne.surf.lobby.velocity.listener.player;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import dev.slne.surf.lobby.velocity.util.LobbyUtil;
import org.jetbrains.annotations.NotNull;

public class PlayerSwitchServerEventListener {

  @Subscribe
  public void onPlayerSwitchServer(@NotNull ServerConnectedEvent event) {
    event.getPreviousServer().ifPresent(LobbyUtil::transferPlayerFromQueue);
  }
}
