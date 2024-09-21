package dev.slne.surf.lobby.bukkit.common.settings;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.data.api.spring.redis.event.annotation.DataListeners;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.RequestSettingsResponseEvent;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.LobbyServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.SurvivalServerData;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.jetbrains.annotations.NotNull;

@DataListeners
public class SettingManager {

  private static EventServerData eventServerData = new EventServerData();
  private static SurvivalServerData survivalServerDataOne = new SurvivalServerData();
  private static SurvivalServerData survivalServerDataTwo = new SurvivalServerData();
  private static ListOrderedMap<String, LobbyServerData> lobbyServerData = new ListOrderedMap<>();

  @DataListener(channels = {RequestSettingsResponseEvent.CHANNEL})
  public void onRequestCurrentServerStateResponse(
      @NotNull RequestSettingsResponseEvent event) {
    eventServerData = event.getEventServerData();
    survivalServerDataOne = event.getSurvivalServerDataOne();
    survivalServerDataTwo = event.getSurvivalServerDataTwo();
    lobbyServerData = event.getLobbyServerData();
  }

  public static SurvivalServerData getSurvivalServerDataOne() {
    return survivalServerDataOne;
  }

  public static SurvivalServerData getSurvivalServerDataTwo() {
    return survivalServerDataTwo;
  }

  public static EventServerData getEventServerData() {
    return eventServerData;
  }

  public static ListOrderedMap<String, LobbyServerData> getLobbyServerData() {
    return lobbyServerData;
  }
}
