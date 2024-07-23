package dev.slne.surf.lobby.core.spring.redis.events.server.lobby;

import dev.slne.surf.lobby.core.spring.redis.events.LobbyRedisEvent;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.CommunityServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.LobbyServerData;
import org.apache.commons.collections4.map.ListOrderedMap;

public class RequestSettingsResponseEvent extends LobbyRedisEvent {

  public static final String CHANNEL = "surf:lobby:request-settings-response";

  private EventServerData eventServerData;
  private CommunityServerData communityServerData;
  private ListOrderedMap<String, LobbyServerData> lobbyServerData;

  private EventServerData eventServerOneData;
  private EventServerData eventServerTwoData;
  private EventServerData eventServerThreeData;
  private EventServerData eventServerFourData;

  public RequestSettingsResponseEvent() {
    super();
  }

  public RequestSettingsResponseEvent(EventServerData eventServerData, CommunityServerData communityServerData, ListOrderedMap<String, LobbyServerData> lobbyServerData, EventServerData eventServerOneData, EventServerData eventServerTwoData, EventServerData eventServerThreeData, EventServerData eventServerFourData) {
    super(CHANNEL);

    this.eventServerData = eventServerData;
    this.communityServerData = communityServerData;
    this.lobbyServerData = lobbyServerData;

    this.eventServerOneData = eventServerOneData;
    this.eventServerTwoData = eventServerTwoData;
    this.eventServerThreeData = eventServerThreeData;
    this.eventServerFourData = eventServerFourData;
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

  public EventServerData getEventServerOneData() {
    return eventServerOneData;
  }

  public EventServerData getEventServerTwoData() {
    return eventServerTwoData;
  }

  public EventServerData getEventServerThreeData() {
    return eventServerThreeData;
  }

  public EventServerData getEventServerFourData() {
    return eventServerFourData;
  }
}
