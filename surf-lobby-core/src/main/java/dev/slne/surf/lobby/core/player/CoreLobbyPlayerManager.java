package dev.slne.surf.lobby.core.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import dev.slne.surf.lobby.api.player.LobbyPlayer;
import dev.slne.surf.lobby.api.player.LobbyPlayerManager;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public abstract class CoreLobbyPlayerManager implements LobbyPlayerManager {

  private final LoadingCache<UUID, LobbyPlayer> playerCache;

  public CoreLobbyPlayerManager() {
    this.playerCache = Caffeine.newBuilder()
        .build(this::createPlayer);
  }

  @Override
  public @NotNull LobbyPlayer getPlayer(@NotNull UUID uuid) {
    return playerCache.get(checkNotNull(uuid, "getUuid"));
  }

  protected abstract LobbyPlayer createPlayer(@NotNull UUID uuid);
}
