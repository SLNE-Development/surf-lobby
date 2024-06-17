package dev.slne.surf.lobby.api.player;

import java.util.UUID;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.NotNull;

@NonExtendable
public interface LobbyPlayerManager {

  @NotNull
  LobbyPlayer getPlayer(@NotNull UUID uuid);
}
