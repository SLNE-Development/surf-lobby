package dev.slne.surf.surfserverselector.velocity.listener.player;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.velocity.util.LobbyUtil;
import org.jetbrains.annotations.NotNull;

public final class PlayerQuitEventListener {

  @Subscribe
  public void onDisconnect(@NotNull DisconnectEvent event) {
    SurfServerSelectorApi.getInstance().getQueueRegistry()
        .removeFromQueue(event.getPlayer().getUniqueId());

    event.getPlayer().getCurrentServer().ifPresent(
        serverConnection -> LobbyUtil.transferPlayerFromQueue(serverConnection.getServer()));
  }
}
