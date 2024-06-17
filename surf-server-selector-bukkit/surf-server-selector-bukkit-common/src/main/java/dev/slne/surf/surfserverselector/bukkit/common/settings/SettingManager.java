package dev.slne.surf.surfserverselector.bukkit.common.settings;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.data.api.spring.redis.event.annotation.DataListeners;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsResponseEvent;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.data.CommunityServerData;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.data.LobbyServerData;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.jetbrains.annotations.NotNull;

@DataListeners
public class SettingManager {

  private static EventServerData eventServerData = new EventServerData();
  private static CommunityServerData communityServerData = new CommunityServerData();
  private static ListOrderedMap<String, LobbyServerData> lobbyServerData = new ListOrderedMap<>();

  @DataListener(channels = {RequestSettingsResponseEvent.CHANNEL})
  public void onRequestCurrentServerStateResponse(
      @NotNull RequestSettingsResponseEvent event) {
    eventServerData = event.getEventServerData();
    communityServerData = event.getCommunityServerData();
    lobbyServerData = event.getLobbyServerData();
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
}
