package dev.slne.surf.lobby.core.spring.redis.events.server.sync;

import dev.slne.data.api.DataApi;
import dev.slne.surf.lobby.core.spring.redis.events.LobbyRedisEvent;

public abstract class SyncEvent extends LobbyRedisEvent {

  private String serverName;

  public SyncEvent() {
    super();
  }

  public SyncEvent(String channel) {
    super(channel);
    this.serverName = DataApi.getDataInstance().getServerName();
  }

  public String getServerName() {
    return serverName;
  }
}
