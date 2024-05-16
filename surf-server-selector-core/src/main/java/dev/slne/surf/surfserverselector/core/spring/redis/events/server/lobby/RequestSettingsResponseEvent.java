package dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby;

import dev.slne.surf.surfserverselector.core.spring.redis.events.ServerSelectorRedisEvent;

public final class RequestSettingsResponseEvent extends ServerSelectorRedisEvent {

  public static final String CHANNEL = "server-selector:lobby:request-settings-response";

  private String eventServerName;
  private boolean eventServerEnabled;
  private String communityServerName;

  public RequestSettingsResponseEvent() {
    super();
  }

  public RequestSettingsResponseEvent(String eventServerName, boolean eventServerEnabled, String communityServerName) {
    super(CHANNEL);

    this.eventServerName = eventServerName;
    this.eventServerEnabled = eventServerEnabled;
    this.communityServerName = communityServerName;
  }

  public boolean isEventServerEnabled() {
    return eventServerEnabled;
  }

  public String getEventServerName() {
    return eventServerName;
  }

  public String getCommunityServerName() {
    return communityServerName;
  }
}
