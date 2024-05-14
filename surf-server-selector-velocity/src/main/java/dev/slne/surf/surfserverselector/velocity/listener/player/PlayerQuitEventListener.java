package dev.slne.surf.surfserverselector.velocity.listener.player;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;

public final class PlayerQuitEventListener {

  @Subscribe
  public void onDisconnect(DisconnectEvent event) {
    SurfServerSelectorApi.getInstance().getQueueRegistry()
        .removeFromQueue(event.getPlayer().getUniqueId());
  }
}
