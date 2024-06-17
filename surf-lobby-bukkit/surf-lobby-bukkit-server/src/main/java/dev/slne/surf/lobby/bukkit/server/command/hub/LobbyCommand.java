package dev.slne.surf.lobby.bukkit.server.command.hub;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.core.message.Messages;
import dev.slne.surf.lobby.core.permissions.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class LobbyCommand extends CommandTree {

  public LobbyCommand(String commandName, String... aliases) {
    super(commandName);

    withPermission(Permissions.COMMAND_HUB_SELF.getPermission());
    withAliases(aliases);

    executesPlayer((PlayerCommandExecutor) (player, args) -> execute(player, player));
    then(new EntitySelectorArgument.OnePlayer("player")
        .withPermission(Permissions.COMMAND_HUB_OTHERS.getPermission())
        .executes(
            (CommandExecutor) (sender, args) -> execute(sender, args.getUnchecked("player"))));
  }

  private void execute(CommandSender sender, @NotNull Player player) {
    LobbyApi.getPlayer(player.getUniqueId()).transferToLobby();

    if (!player.equals(sender)) {
      Messages.TRANSFERRING_PLAYER_TO_LOBBY.send(sender, player.displayName());
    }
  }
}
