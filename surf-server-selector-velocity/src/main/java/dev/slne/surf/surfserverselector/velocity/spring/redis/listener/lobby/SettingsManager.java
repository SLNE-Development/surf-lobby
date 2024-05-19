package dev.slne.surf.surfserverselector.velocity.spring.redis.listener.lobby;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsEvent;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsResponseEvent;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;
import dev.slne.surf.surfserverselector.velocity.config.VelocityPersistentData;
import dev.slne.surf.surfserverselector.velocity.util.LobbyUtil;
import java.util.List;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public final class SettingsManager {

  @DataListener(channels = {RequestSettingsEvent.CHANNEL})
  public void onRequestCurrentServerState(RequestSettingsEvent __) {
    update();
  }

  public static void update() {
    final VelocityConfig config = VelocityConfig.get();

    final List<String> lobbyServerNames = LobbyUtil.getAllLobbyServer().stream()
        .map(server -> server.getServerInfo().getName())
        .sorted()
        .toList();

    new RequestSettingsResponseEvent(
        config.currentEventServer(),
        VelocityPersistentData.get().isEventServerEnabled(),
        config.communityServerName(),
        lobbyServerNames
    ).call();
  }
}
