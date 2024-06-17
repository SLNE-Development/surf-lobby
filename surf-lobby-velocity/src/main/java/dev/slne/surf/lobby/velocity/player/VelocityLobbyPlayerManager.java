package dev.slne.surf.lobby.velocity.player;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.lobby.api.player.LobbyPlayer;
import dev.slne.surf.lobby.core.player.CoreLobbyPlayerManager;
import java.util.UUID;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class VelocityLobbyPlayerManager extends CoreLobbyPlayerManager {

  public VelocityLobbyPlayerManager() {
  }

  @Contract("_ -> new")
  @Override
  protected @NotNull LobbyPlayer createPlayer(@NotNull UUID uuid) {
    return new VelocityLobbyPlayer(checkNotNull(uuid, "getUuid"));
  }
}
