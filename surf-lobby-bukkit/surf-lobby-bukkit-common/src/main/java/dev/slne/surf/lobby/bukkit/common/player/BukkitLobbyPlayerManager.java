package dev.slne.surf.lobby.bukkit.common.player;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.lobby.api.player.LobbyPlayer;
import dev.slne.surf.lobby.core.player.CoreLobbyPlayerManager;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class BukkitLobbyPlayerManager extends CoreLobbyPlayerManager {

  public BukkitLobbyPlayerManager() {
  }

  @Contract("_ -> new")
  @Override
  protected @NotNull LobbyPlayer createPlayer(@NotNull UUID uuid) {
    return new BukkitLobbyPlayer(checkNotNull(uuid, "getUuid"));
  }
}
