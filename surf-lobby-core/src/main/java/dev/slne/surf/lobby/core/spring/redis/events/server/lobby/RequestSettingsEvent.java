package dev.slne.surf.lobby.core.spring.redis.events.server.lobby;

import dev.slne.surf.lobby.core.spring.redis.events.LobbyRedisEvent;

public class RequestSettingsEvent extends LobbyRedisEvent {

  public static final String CHANNEL = "surf:lobby:request-settings";

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
