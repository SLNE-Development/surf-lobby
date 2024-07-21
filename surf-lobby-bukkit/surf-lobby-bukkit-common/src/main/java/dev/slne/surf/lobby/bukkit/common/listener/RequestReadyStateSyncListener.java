package dev.slne.surf.lobby.bukkit.common.listener;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.lobby.core.spring.redis.events.server.sync.ReadyStateSync;
import dev.slne.surf.lobby.core.spring.redis.events.server.sync.RequestReadyStateSync;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public class RequestReadyStateSyncListener {

  @DataListener(channels = {RequestReadyStateSync.CHANNEL})
  public void onRequestReadyStateSync(RequestReadyStateSync event) {
    new ReadyStateSync(true).call(); // TODO: 19.07.2024 00:07 - properly implement this
  }
}
