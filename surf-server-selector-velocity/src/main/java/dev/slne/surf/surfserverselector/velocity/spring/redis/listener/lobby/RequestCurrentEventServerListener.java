package dev.slne.surf.surfserverselector.velocity.spring.redis.listener.lobby;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsEvent;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsResponseEvent;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public final class RequestCurrentEventServerListener {

  @DataListener(channels = {RequestSettingsEvent.CHANNEL})
  public void onRequestCurrentServerState(RequestSettingsEvent event) {
    final VelocityConfig config = VelocityConfig.get();

    new RequestSettingsResponseEvent(config.currentEventServer(),
        config.eventServerEnabled(), config.communityServerName()).call();
  }
}
