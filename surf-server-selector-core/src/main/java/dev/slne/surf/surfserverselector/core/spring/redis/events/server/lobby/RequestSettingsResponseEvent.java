package dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby;

import dev.slne.surf.surfserverselector.core.spring.redis.events.ServerSelectorRedisEvent;
import java.util.List;

public final class RequestSettingsResponseEvent extends ServerSelectorRedisEvent {

  public static final String CHANNEL = "server-selector:lobby:request-settings-response";

  private String eventServerName;
  private boolean eventServerEnabled;
  private String communityServerName;
  private List<String> lobbyServerNames;

  public RequestSettingsResponseEvent() {
    super();
  }

  public RequestSettingsResponseEvent(String eventServerName, boolean eventServerEnabled, String communityServerName, List<String> lobbyServerNames) {
    super(CHANNEL);

    this.eventServerName = eventServerName;
    this.eventServerEnabled = eventServerEnabled;
    this.communityServerName = communityServerName;
    this.lobbyServerNames = lobbyServerNames;
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

  public List<String> getLobbyServerNames() {
    return lobbyServerNames;
  }
}
