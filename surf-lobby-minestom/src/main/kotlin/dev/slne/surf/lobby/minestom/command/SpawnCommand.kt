package dev.slne.surf.lobby.minestom.command

import dev.slne.minestom.lobby.api.command.commandapi.dsl.commandTree
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutor
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendSpawnTeleported
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.minestom.location.toPos

fun spawnCommand() = commandTree("spawn") {
    withPermission(LobbyPermissions.COMMAND_SPAWN)
    playerExecutor { player, _ ->
        player.teleport(lobbyConfig.spawnPoint.toPos()).thenRun {
            player.sendSpawnTeleported()
        }
    }
}
