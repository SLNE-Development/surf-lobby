package dev.slne.surf.lobby.bukkit.common.settings;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.data.api.spring.redis.event.annotation.DataListeners;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.RequestSettingsResponseEvent;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.CommunityServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.LobbyServerData;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.jetbrains.annotations.NotNull;

@DataListeners
public class SettingManager {

  private static EventServerData eventServerData = new EventServerData();
  private static CommunityServerData communityServerData = new CommunityServerData();
  private static ListOrderedMap<String, LobbyServerData> lobbyServerData = new ListOrderedMap<>();

  private static EventServerData eventServerOneData = new EventServerData();
  private static EventServerData eventServerTwoData = new EventServerData();
  private static EventServerData eventServerThreeData = new EventServerData();
  private static EventServerData eventServerFourData = new EventServerData();

  @DataListener(channels = {RequestSettingsResponseEvent.CHANNEL})
  public void onRequestCurrentServerStateResponse(
      @NotNull RequestSettingsResponseEvent event) {
    eventServerData = event.getEventServerData();
    communityServerData = event.getCommunityServerData();
    lobbyServerData = event.getLobbyServerData();

    eventServerOneData = event.getEventServerOneData();
    eventServerTwoData = event.getEventServerTwoData();
    eventServerThreeData = event.getEventServerThreeData();
    eventServerFourData = event.getEventServerFourData();
  }

  public static CommunityServerData getCommunityServerData() {
    return communityServerData;
  }

  public static EventServerData getEventServerData() {
    return eventServerData;
  }

  public static ListOrderedMap<String, LobbyServerData> getLobbyServerData() {
    return lobbyServerData;
  }

  public static EventServerData getEventServerOneData() {
    return eventServerOneData;
  }

  public static EventServerData getEventServerTwoData() {
    return eventServerTwoData;
  }

  public static EventServerData getEventServerThreeData() {
    return eventServerThreeData;
  }

  public static EventServerData getEventServerFourData() {
    return eventServerFourData;
  }
}
