package dev.slne.surf.surfserverselector.core.spring.redis.events.server.sync;

import dev.slne.data.api.DataApi;
import dev.slne.surf.surfserverselector.core.spring.redis.events.ServerSelectorRedisEvent;

public abstract class SyncEvent extends ServerSelectorRedisEvent {

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
