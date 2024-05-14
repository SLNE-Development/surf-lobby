package dev.slne.surf.surfserverselector.core.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayerManager;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class CoreServerSelectorPlayerManager implements ServerSelectorPlayerManager {

  private final LoadingCache<UUID, ServerSelectorPlayer> playerCache;

  public CoreServerSelectorPlayerManager() {
    this.playerCache = Caffeine.newBuilder()
        .build(this::createPlayer);
  }

  @Override
  public @NotNull ServerSelectorPlayer getPlayer(@NotNull UUID uuid) {
    return playerCache.get(checkNotNull(uuid, "getUuid"));
  }

  protected abstract ServerSelectorPlayer createPlayer(@NotNull UUID uuid);
}
