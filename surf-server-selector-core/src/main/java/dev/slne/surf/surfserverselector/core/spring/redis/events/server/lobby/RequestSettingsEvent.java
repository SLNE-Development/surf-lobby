package dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby;

import dev.slne.surf.surfserverselector.core.spring.redis.events.ServerSelectorRedisEvent;

public final class RequestSettingsEvent extends ServerSelectorRedisEvent {

  public static final String CHANNEL = "server-selector:lobby:request-settings";

  private String dummy;

  public RequestSettingsEvent() {
  }

  public RequestSettingsEvent(String dummy) {
    super(CHANNEL);

    this.dummy = dummy;
  }

  public String getDummy() {
    return dummy;
  }
}
