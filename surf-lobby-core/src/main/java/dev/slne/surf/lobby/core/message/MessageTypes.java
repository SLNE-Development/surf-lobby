package dev.slne.surf.lobby.core.message;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.surfapi.core.api.messages.Colors;
import dev.slne.surf.lobby.api.player.LobbyPlayer;
import java.util.Arrays;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

public final class MessageTypes {

  @Internal
  public interface Message {

    @NotNull
    ComponentLike message(ComponentLike... args);

    void send(@NotNull Audience audience, ComponentLike... args);

    default void send(@NotNull LobbyPlayer player, ComponentLike... args) {
      checkNotNull(player, "player").sendMessage(this.message(colorArgs(args)));
    }
  }

  private interface SimpleMessage extends Message {

    @NotNull
    String simpleMessage();

    @Override
    default @NotNull ComponentLike message(ComponentLike... args) {
      return Component.text(this.simpleMessage());
    }
  }

  @FunctionalInterface
  public interface ErrorMessage extends Message {

    default void send(@NotNull Audience audience, ComponentLike... args) {
      sendMessage(audience,
          this.message(colorArgs(args)).asComponent().colorIfAbsent(Colors.ERROR));
    }
  }

  @FunctionalInterface
  public interface SimpleErrorMessage extends ErrorMessage, SimpleMessage {

  }

  @FunctionalInterface
  public interface SuccessMessage extends Message {

    default void send(@NotNull Audience audience, ComponentLike... args) {
      sendMessage(audience,
          this.message(colorArgs(args)).asComponent().colorIfAbsent(Colors.SUCCESS));
    }
  }

  @FunctionalInterface
  public interface SimpleSuccessMessage extends SuccessMessage, SimpleMessage {

  }

  @FunctionalInterface
  public interface InfoMessage extends Message {

    default void send(@NotNull Audience audience, ComponentLike... args) {
      sendMessage(audience,
          this.message(colorArgs(args)).asComponent().colorIfAbsent(Colors.INFO));
    }
  }

  @FunctionalInterface
  public interface SimpleInfoMessage extends InfoMessage, SimpleMessage {

  }

  private static ComponentLike[] colorArgs(ComponentLike... args) {
    return Arrays.stream(args).map(arg -> arg.asComponent().colorIfAbsent(Colors.VARIABLE_VALUE))
        .toArray(ComponentLike[]::new);
  }

  private static void sendMessage(@NotNull Audience audience, @NotNull Component message) {
    checkNotNull(audience, "audience").sendMessage(
        Colors.PREFIX.append(checkNotNull(message, "message")));
  }
}
