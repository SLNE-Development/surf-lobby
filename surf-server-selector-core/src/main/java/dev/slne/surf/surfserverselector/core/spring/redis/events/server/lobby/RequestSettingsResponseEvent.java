package dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby;

import dev.slne.surf.surfserverselector.core.spring.redis.events.ServerSelectorRedisEvent;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.data.CommunityServerData;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.data.LobbyServerData;
import org.apache.commons.collections4.map.ListOrderedMap;

public class RequestSettingsResponseEvent extends ServerSelectorRedisEvent {

  public static final String CHANNEL = "server-selector:lobby:request-settings-response";

  private EventServerData eventServerData;
  private CommunityServerData communityServerData;
  private ListOrderedMap<String, LobbyServerData> lobbyServerData;

  public RequestSettingsResponseEvent() {
    super();
  }

  public RequestSettingsResponseEvent(EventServerData eventServerData, CommunityServerData communityServerData, ListOrderedMap<String, LobbyServerData> lobbyServerData) {
    super(CHANNEL);

    this.eventServerData = eventServerData;
    this.communityServerData = communityServerData;
    this.lobbyServerData = lobbyServerData;
  }

  public EventServerData getEventServerData() {
    return eventServerData;
  }

  public CommunityServerData getCommunityServerData() {
    return communityServerData;
  }

  public ListOrderedMap<String, LobbyServerData> getLobbyServerData() {
    return lobbyServerData;
  }
}
