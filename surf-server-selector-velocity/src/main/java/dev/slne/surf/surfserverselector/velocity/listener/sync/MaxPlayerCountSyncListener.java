package dev.slne.surf.surfserverselector.velocity.listener.sync;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.sync.MaxPlayerCountSync;
import dev.slne.surf.surfserverselector.velocity.sync.SyncValue;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public class MaxPlayerCountSyncListener {

  @DataListener(channels = {MaxPlayerCountSync.CHANNEL})
  public void onMaxPlayerCountSync(MaxPlayerCountSync event) {
    SyncValue.MAX_PLAYER_COUNT.sync(event.getServerName(), event.getMaxPlayerCount());
  }
}
