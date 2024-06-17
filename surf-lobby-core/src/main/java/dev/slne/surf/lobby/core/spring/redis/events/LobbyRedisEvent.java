package dev.slne.surf.lobby.core.spring.redis.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.slne.data.api.spring.redis.event.RedisEvent;
import dev.slne.surf.lobby.api.LobbyApi;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class LobbyRedisEvent extends RedisEvent {

  public LobbyRedisEvent() {
  }

  public LobbyRedisEvent(String... channels) {
    super(channels);
  }

  @JsonIgnore
  public void call() {
    this.call(LobbyApi.getContext());
  }

  @Override
  @JsonIgnore
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
