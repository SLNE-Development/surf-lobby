package dev.slne.surf.lobby.core.spring.redis.events.server.lobby;

import dev.slne.surf.lobby.core.spring.redis.events.LobbyRedisEvent;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.BmbfServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.LobbyServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.SurvivalServerData;
import org.apache.commons.collections4.map.ListOrderedMap;

public class RequestSettingsResponseEvent extends LobbyRedisEvent {

  public static final String CHANNEL = "surf:lobby:request-settings-response";

  private EventServerData eventServerData;
  private BmbfServerData bmbfServerData;
  private SurvivalServerData survivalServerDataOne;
  private SurvivalServerData survivalServerDataTwo;
  private ListOrderedMap<String, LobbyServerData> lobbyServerData;

  public RequestSettingsResponseEvent() {
    super();
  }

  public RequestSettingsResponseEvent(
      EventServerData eventServerData,
      BmbfServerData bmbfServerData,
      SurvivalServerData survivalServerDataOne,
      SurvivalServerData survivalServerDataTwo,
      ListOrderedMap<String, LobbyServerData> lobbyServerData
  ) {
    super(CHANNEL);

    this.eventServerData = eventServerData;
    this.bmbfServerData = bmbfServerData;
    this.survivalServerDataOne = survivalServerDataOne;
    this.survivalServerDataTwo = survivalServerDataTwo;
    this.lobbyServerData = lobbyServerData;
  }

  public EventServerData getEventServerData() {
    return eventServerData;
  }

  public BmbfServerData getBmbfServerData() {
    return bmbfServerData;
  }

  public SurvivalServerData getSurvivalServerDataOne() {
    return survivalServerDataOne;
  }

  public SurvivalServerData getSurvivalServerDataTwo() {
    return survivalServerDataTwo;
  }

  public ListOrderedMap<String, LobbyServerData> getLobbyServerData() {
    return lobbyServerData;
  }
}
