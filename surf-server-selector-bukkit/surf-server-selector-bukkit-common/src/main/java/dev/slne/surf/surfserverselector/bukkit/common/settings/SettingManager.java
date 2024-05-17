package dev.slne.surf.surfserverselector.bukkit.common.settings;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsResponseEvent;
import org.jetbrains.annotations.NotNull;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public final class SettingManager {

  private static String currentEventServer = "";
  private static boolean eventServerEnabled = false;

  private static String communityServer = "";


  @DataListener(channels = {RequestSettingsResponseEvent.CHANNEL})
  public void onRequestCurrentServerStateResponse(
      @NotNull RequestSettingsResponseEvent event) {
    currentEventServer = event.getEventServerName();
    eventServerEnabled = event.isEventServerEnabled();
    communityServer = event.getCommunityServerName();
  }

  public static String getCurrentEventServer() {
    return currentEventServer;
  }

  public static boolean isEventServerEnabled() {
    return eventServerEnabled;
  }

  public static String getCommunityServer() {
    return communityServer;
  }
}
