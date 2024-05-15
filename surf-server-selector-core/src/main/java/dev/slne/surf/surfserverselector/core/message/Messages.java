package dev.slne.surf.surfserverselector.core.message;

import static net.kyori.adventure.text.Component.text;

import dev.slne.surf.surfserverselector.core.message.MessageTypes.ErrorMessage;
import dev.slne.surf.surfserverselector.core.message.MessageTypes.InfoMessage;
import dev.slne.surf.surfserverselector.core.message.MessageTypes.SimpleErrorMessage;
import dev.slne.surf.surfserverselector.core.message.MessageTypes.SuccessMessage;

public interface Messages {

  SimpleErrorMessage ALREADY_CHANGING_SERVER = () -> "Du wechselst bereits den Server!";

  ErrorMessage LOBBY_SERVER_NOT_AVAILABLE = args -> text()
      .append(text("Der Lobby-Server "))
      .append(args[0])
      .append(text(" ist nicht verfügbar!"));

  SuccessMessage CHANGED_TO_LOBBY_SERVER = args -> text()
      .append(text("Du bist jetzt auf dem Lobby-Server "))
      .append(args[0])
      .append(text("!"));

  ErrorMessage ERROR_CHANGING_SERVER = args -> text()
      .append(text("Es ist ein Fehler beim Wechseln auf den Server "))
      .append(args[0])
      .append(text(" aufgetreten!"));

  SuccessMessage CHANGED_TO_SERVER = args -> text()
      .append(text("Du befindest dich jetzt auf dem Server "))
      .append(args[0])
      .append(text("!"));
  SuccessMessage CHANGED_TO_FULL_SERVER_WITH_BYPASS_PERMISSION = args -> text()
      .append(text("Du befindest dich jetzt auf dem Server "))
      .append(args[0])
      .append(text(" (Bypass-Permission)!"));

  SuccessMessage CHANGED_TO_OTHER_LOBBY_AS_REQUESTED = args -> text()
      .append(text(
          "Du befindest dich jetzt auf einem anderen Lobby-Server als angefordert, da dieser zu voll war: "))
      .append(args[0])
      .append(text("!"));

  InfoMessage JOINED_QUEUE = args -> text()
      .append(text("Du bist nun in der Warteschlange für den Server "))
      .append(args[0])
      .append(text("!"));

  ErrorMessage COULD_NOT_ESTABLISH_CONNECTION_WITH_SERVER = args -> text()
      .append(text("Es konnte keine Verbindung zum Server "))
      .append(args[0])
      .append(text(" hergestellt werden!"));

  ErrorMessage ALREADY_IN_QUEUE = args -> text()
      .append(text("Du bist bereits in der Warteschlange für den Server "))
      .append(args[0])
      .append(text("!"));

  ErrorMessage LOBBY_SERVER_FULL = args -> text()
      .append(text("Der Lobby-Server "))
      .append(args[0])
      .append(text(" ist voll!"));

  ErrorMessage ALREADY_CONNECTED_TO_SERVER = args -> text()
      .append(text("Du bist bereits mit dem Server "))
      .append(args[0])
      .append(text(" verbunden!"));
}
