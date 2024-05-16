package dev.slne.surf.surfserverselector.core.spring.redis.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.slne.data.api.spring.redis.event.RedisEvent;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class ServerSelectorRedisEvent extends RedisEvent {

  public ServerSelectorRedisEvent() {
  }

  public ServerSelectorRedisEvent(String... channels) {
    super(channels);
  }

  @JsonIgnore
  public void call() {
    this.call(SurfServerSelectorApi.getContext());
  }

  @Override
  @JsonIgnore
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
