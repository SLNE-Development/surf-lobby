package dev.slne.surf.surfserverselector.core.player;

import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import java.util.UUID;

public abstract class CoreServerSelectorPlayer implements ServerSelectorPlayer {

  protected final UUID uuid;

  public CoreServerSelectorPlayer(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public UUID uuid() {
    return this.uuid;
  }
}
