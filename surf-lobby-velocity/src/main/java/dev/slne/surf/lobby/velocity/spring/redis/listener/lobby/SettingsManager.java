package dev.slne.surf.lobby.velocity.spring.redis.listener.lobby;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.RequestSettingsEvent;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.RequestSettingsResponseEvent;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.CommunityServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.LobbyServerData;
import dev.slne.surf.lobby.velocity.VelocityMain;
import dev.slne.surf.lobby.velocity.config.VelocityConfig;
import dev.slne.surf.lobby.velocity.config.VelocityPersistentData;
import dev.slne.surf.lobby.velocity.util.LobbyUtil;
import dev.slne.surf.lobby.velocity.sync.SyncValue;
import org.apache.commons.collections4.map.ListOrderedMap;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public final class SettingsManager {

  @DataListener(channels = {RequestSettingsEvent.CHANNEL})
  public void onRequestCurrentServerState(RequestSettingsEvent __) {
    update();
  }

  public static void update() {
    final VelocityConfig config = VelocityConfig.get();

    final EventServerData eventServerData = EventServerData.of(
        config.currentEventServer(),
        VelocityPersistentData.get().isEventServerEnabled(),
        SyncValue.MAX_PLAYER_COUNT.get(config.currentEventServer()),
        getOnlinePlayerCount(config.currentEventServer())
    );

    final CommunityServerData communityServerData = CommunityServerData.of(
        config.communityServerName(),
        SyncValue.MAX_PLAYER_COUNT.get(config.communityServerName()),
        getOnlinePlayerCount(config.communityServerName())
    );

    final ListOrderedMap<String, LobbyServerData> lobbyServerNames = LobbyUtil.getAllLobbyServer()
        .stream()
        .map(server -> {
          final String serverName = server.getServerInfo().getName();

          return LobbyServerData.of(
              serverName,
              SyncValue.MAX_PLAYER_COUNT.get(serverName),
              getOnlinePlayerCount(serverName)
          );
        })
        .sorted()
        .collect(LobbyServerData.toMap());

    new RequestSettingsResponseEvent(
        eventServerData,
        communityServerData,
        lobbyServerNames
    ).call();
  }

  private static int getOnlinePlayerCount(String serverName) {
    return VelocityMain.getInstance().getServer().getServer(serverName)
        .map(server -> server.getPlayersConnected().size())
        .orElse(-1);
  }
}
