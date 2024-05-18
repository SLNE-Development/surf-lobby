package dev.slne.surf.surfserverselector.velocity.listener.sync;

import dev.slne.data.api.spring.redis.event.annotation.DataListener;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.sync.ReadyStateSync;
import dev.slne.surf.surfserverselector.velocity.sync.SyncValue;

@dev.slne.data.api.spring.redis.event.annotation.DataListeners
public class ReadyStateSyncListener {

  @DataListener(channels = {ReadyStateSync.CHANNEL})
  public void onReadyStateSync(ReadyStateSync event) {
    SyncValue.READY_STATE.sync(event.getServerName(), event.getReadyState());
  }
}
