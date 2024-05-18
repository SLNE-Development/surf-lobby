package dev.slne.surf.surfserverselector.core.spring.redis.events.server.sync;

public class ReadyStateSync extends SyncEvent{
  public static final String CHANNEL = "server-selector:sync:ready-state";

  private boolean readyState;

  public ReadyStateSync() {
    super();
  }

  public ReadyStateSync(boolean readyState) {
    super(CHANNEL);

    this.readyState = readyState;
  }

  public boolean getReadyState() {
    return readyState;
  }
}
