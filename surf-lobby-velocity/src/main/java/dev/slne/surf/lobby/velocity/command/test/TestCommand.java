package dev.slne.surf.lobby.velocity.command.test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.velocity.VelocityMain;

public class TestCommand extends CommandAPICommand {

  public TestCommand(String commandName) {
    super(commandName);

    withArguments(new StringArgument("server")
        .replaceSuggestions(ArgumentSuggestions.stringCollection(
            info -> VelocityMain.getInstance().getServer().getAllServers().stream()
                .map(server -> server.getServerInfo().getName()).toList())));

    executesPlayer((player, commandArguments) -> {
      final String serverName = commandArguments.getUnchecked("server");

      LobbyApi.getPlayer(player.getUniqueId()).changeServer(serverName, true, true);
    });
  }
}
