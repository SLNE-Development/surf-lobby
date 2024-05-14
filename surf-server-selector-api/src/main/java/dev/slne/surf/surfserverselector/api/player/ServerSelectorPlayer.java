package dev.slne.surf.surfserverselector.api.player;

import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface ServerSelectorPlayer {

  UUID uuid();

  Optional<? extends Audience> player();
}
