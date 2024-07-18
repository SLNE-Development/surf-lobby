package dev.slne.surf.lobby.core.spring.redis.events.server.sync;

public class RequestReadyStateSync extends SyncEvent{
  public static final String CHANNEL = "surf:lobby:sync:ready-state-request";

  public RequestReadyStateSync() {
  }

  public RequestReadyStateSync(Object dummy) {
    super(CHANNEL);
  }
}
