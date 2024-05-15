package dev.slne.surf.surfserverselector.api.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NonExtendable
public interface ServerSelectorPlayer {

  UUID getUuid();

  Optional<? extends Audience> getPlayer();

  void changeServer(@Nullable String serverName, boolean sendFeedback);

  int getPriority(); // TODO: use luckperms group weight

  default void sendMessage(@NotNull ComponentLike message) {
    this.getPlayer().ifPresent(player -> player.sendMessage(checkNotNull(message, "message")));
  }
}
