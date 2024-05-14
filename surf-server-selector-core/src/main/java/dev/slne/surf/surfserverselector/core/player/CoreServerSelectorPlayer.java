package dev.slne.surf.surfserverselector.core.player;

import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import java.util.UUID;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public abstract class CoreServerSelectorPlayer implements ServerSelectorPlayer {
  private static final String VANISH_LEVEL_META_DATA = "surf.server.selector.queue.priority";

  protected final UUID uuid;

  public CoreServerSelectorPlayer(UUID uuid) {
    this.uuid = uuid;
  }

  @Override
  public UUID getUuid() {
    return this.uuid;
  }

  @Override
  public int getPriority() { // TODO: 14.05.2024 18:17 - group?
    final User user = LuckPermsProvider.get().getUserManager().getUser(uuid);

    if (user != null) {
      return user.getCachedData().getMetaData().getMetaValue(VANISH_LEVEL_META_DATA, Integer::parseInt).orElse(-1);
    }

    return -1;
  }
}
