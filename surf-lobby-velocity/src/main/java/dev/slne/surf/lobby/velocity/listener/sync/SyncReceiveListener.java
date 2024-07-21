package dev.slne.surf.lobby.velocity.listener.sync;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.data.api.spring.redis.event.annotation.DataListeners;
import dev.slne.surf.lobby.core.spring.redis.events.server.sync.MaxPlayerCountSync;
import dev.slne.surf.lobby.core.spring.redis.events.server.sync.ReadyStateSync;
import dev.slne.surf.lobby.velocity.sync.SyncValue;

@DataListeners
public class SyncReceiveListener {

  @DataListener(channels = {ReadyStateSync.CHANNEL})
  public void onReadyStateSync(ReadyStateSync event) {
    SyncValue.READY_STATE.sync(event.getServerName(), event.getReadyState());
  }

  @DataListener(channels = {MaxPlayerCountSync.CHANNEL})
  public void onMaxPlayerCountSync(MaxPlayerCountSync event) {
    String serverName = event.getServerName();

    SyncValue.MAX_PLAYER_COUNT.sync(serverName, event.getMaxPlayerCount());
  }
}
