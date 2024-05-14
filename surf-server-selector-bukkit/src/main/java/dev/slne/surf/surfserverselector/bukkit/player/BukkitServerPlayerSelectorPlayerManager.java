package dev.slne.surf.surfserverselector.bukkit.player;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.surfserverselector.api.player.ServerSelectorPlayer;
import dev.slne.surf.surfserverselector.core.player.CoreServerSelectorPlayerManager;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class BukkitServerPlayerSelectorPlayerManager extends CoreServerSelectorPlayerManager {

  public BukkitServerPlayerSelectorPlayerManager() {
  }

  @Contract("_ -> new")
  @Override
  protected @NotNull ServerSelectorPlayer createPlayer(@NotNull UUID uuid) {
    return new BukkitServerSelectorPlayer(checkNotNull(uuid, "getUuid"));
  }
}
