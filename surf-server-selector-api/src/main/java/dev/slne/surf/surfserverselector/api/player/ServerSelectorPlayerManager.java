package dev.slne.surf.surfserverselector.api.player;

import java.util.UUID;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.NotNull;

@NonExtendable
public interface ServerSelectorPlayerManager {

  @NotNull
  ServerSelectorPlayer getPlayer(@NotNull UUID uuid);
}
