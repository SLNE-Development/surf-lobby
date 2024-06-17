package dev.slne.surf.lobby.velocity.command.event;

import static net.kyori.adventure.text.Component.text;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.slne.surf.lobby.core.message.Messages;
import dev.slne.surf.lobby.core.permissions.Permissions;
import dev.slne.surf.lobby.velocity.config.VelocityPersistentData;

public class SwitchEventServerState extends CommandAPICommand {

  public SwitchEventServerState(String commandName) {
    super(commandName);

    withPermission(Permissions.COMMAND_SWITCH_EVENT_SERVER_STATE.getPermission());

    withArguments(new BooleanArgument("enable")
        .replaceSuggestions(ArgumentSuggestions.strings(info ->
            new String[]{VelocityPersistentData.get().isEventServerEnabled() ? "false" : "true"})));

    executes((CommandExecutor) (sender, args) -> switchState(sender,
        Boolean.TRUE.equals(args.getUnchecked("enable"))));
  }

  private void switchState(CommandSource sender, boolean enable) {
    VelocityPersistentData.get().setEventServerEnabled(enable);

    Messages.EVENT_SERVER_STATE_SWITCHED.send(sender,
        enable ? text("aktiviert") : text("deaktiviert"));
  }
}
