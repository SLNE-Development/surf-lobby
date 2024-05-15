package dev.slne.surf.surfserverselector.velocity.spring.redis.listener;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.change.RequestChangeServerEvent;
import org.jetbrains.annotations.NotNull;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public final class RequestChangeServerEventListener {

  @DataListener(channels = {RequestChangeServerEvent.CHANNEL})
  public void onRequestChangeServer(@NotNull RequestChangeServerEvent event) {
    SurfServerSelectorApi.getPlayer(event.getUuid())
        .changeServer(event.getRequestedServerName(), event.isSendFeedback(),
            event.isFallbackToLobbyWithLowestPlayerCount());
  }
}
