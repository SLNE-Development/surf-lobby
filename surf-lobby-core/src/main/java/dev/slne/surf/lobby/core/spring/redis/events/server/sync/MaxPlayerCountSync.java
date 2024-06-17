package dev.slne.surf.lobby.core.spring.redis.events.server.sync;

public class MaxPlayerCountSync extends SyncEvent {
  public static final String CHANNEL = "surf:sync:max-player-count";

  private int maxPlayerCount;

  public MaxPlayerCountSync() {
    super();
  }

  public MaxPlayerCountSync(int maxPlayerCount) {
    super(CHANNEL);

    this.maxPlayerCount = maxPlayerCount;
  }

  public int getMaxPlayerCount() {
    return maxPlayerCount;
  }
}
