package dev.slne.surf.surfserverselector.bukkit.server.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.ExecutableCommand;
import dev.slne.surf.surfserverselector.bukkit.server.command.hub.LobbyCommand;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class CommandManager {
  public static final CommandManager INSTANCE = new CommandManager();

  private final ObjectList<ExecutableCommand<?, ?>> commands = new ObjectArrayList<>();

  private CommandManager() {
  }

  @Contract(pure = true)
  public void registerCommands() {
    register(new LobbyCommand("lobby", "hub"));
  }

  public void unregisterCommands() {
    commands.forEach(commandTree -> CommandAPI.unregister(commandTree.getName()));
  }

  private void register(@NotNull CommandTree commandTree) {
    commandTree.register();
    this.commands.add(commandTree);
  }
}
