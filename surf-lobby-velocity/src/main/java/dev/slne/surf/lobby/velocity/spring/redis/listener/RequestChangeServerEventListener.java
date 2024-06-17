package dev.slne.surf.lobby.velocity.spring.redis.listener;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.core.spring.redis.events.server.change.RequestChangeServerEvent;
import org.jetbrains.annotations.NotNull;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public final class RequestChangeServerEventListener {

  @DataListener(channels = {RequestChangeServerEvent.CHANNEL})
  public void onRequestChangeServer(@NotNull RequestChangeServerEvent event) {
    LobbyApi.getPlayer(event.getUuid())
        .changeServer(event.getRequestedServerName(), event.isSendFeedback(),
            event.isFallbackToLobbyWithLowestPlayerCount());
  }
}
