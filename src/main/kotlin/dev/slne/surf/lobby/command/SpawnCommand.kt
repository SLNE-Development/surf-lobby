package dev.slne.surf.lobby.command

import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.utils.PermissionRegistry

fun spawnCommand() = commandTree("spawn") {
    withPermission(PermissionRegistry.COMMAND_SPAWN)
    playerExecutor { player, _ ->
        player.teleportAsync(lobbyConfig.spawnPoint.toLocation()).thenRun {
            player.sendText {
                appendSuccessPrefix()
                success("Du wurdest zum Spawn teleportiert.")
            }
        }
    }
}