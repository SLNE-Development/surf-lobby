package dev.slne.surf.lobby.velocity.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.slne.surf.lobby.velocity.command.event.SwitchEventServerState;
import dev.slne.surf.lobby.velocity.command.test.TestCommand;
import java.util.List;

public final class CommandManager {

  public static final CommandManager INSTANCE = new CommandManager();

  private final List<CommandAPICommand> commands;

  public CommandManager() {
    this.commands = List.of(
        new TestCommand("test-lobby-server-selector"),
        new SwitchEventServerState("switch-event-server-state")
    );
  }

  public void registerCommands() {
    this.commands.forEach(CommandAPICommand::register);
  }

  public void unregisterCommands() {
    this.commands.forEach(command -> CommandAPI.unregister(command.getName()));
  }
}
