package dev.slne.surf.surfserverselector.api.player;

import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.Nullable;

@NonExtendable
public interface ServerSelectorPlayer {

  UUID getUuid();

  Optional<? extends Audience> getPlayer();

  void changeServer(@Nullable String serverName);

  int getPriority(); // TODO: use luckperms group weight
}
