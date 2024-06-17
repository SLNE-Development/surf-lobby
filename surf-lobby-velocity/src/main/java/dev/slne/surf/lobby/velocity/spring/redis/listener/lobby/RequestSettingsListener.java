package dev.slne.surf.lobby.velocity.spring.redis.listener.lobby;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.data.api.spring.redis.event.annotation.DataListeners;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.RequestSettingsEvent;

@DataListeners
public final class RequestSettingsListener {

  @DataListener(channels = {RequestSettingsEvent.CHANNEL})
  public void onRequestCurrentServerState(RequestSettingsEvent __) {
    SettingsSyncTask.update();
  }
}
