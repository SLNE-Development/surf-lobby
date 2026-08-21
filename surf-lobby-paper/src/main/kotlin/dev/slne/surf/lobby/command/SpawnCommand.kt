package dev.slne.surf.lobby.command

import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.lobby.config.toLocation
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendSpawnTeleported
import dev.slne.surf.lobby.utils.PermissionRegistry

fun spawnCommand() = commandTree("spawn") {
    withPermission(PermissionRegistry.COMMAND_SPAWN)
    playerExecutor { player, _ ->
        player.teleportAsync(lobbyConfig.spawnPoint.toLocation()).thenRun {
            player.sendSpawnTeleported()
        }
    }
}
