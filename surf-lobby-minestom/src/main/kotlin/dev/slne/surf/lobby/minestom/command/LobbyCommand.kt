package dev.slne.surf.lobby.minestom.command

import dev.slne.surf.api.minestom.command.dsl.anyExecutor
import dev.slne.surf.api.minestom.command.dsl.commandTree
import dev.slne.surf.api.minestom.command.dsl.literalArgument
import dev.slne.surf.lobby.core.client.config.lobbyConfigHolder
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendConfigReloaded
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions

fun lobbyCommand() = commandTree("surflobby") {
    withPermission(LobbyPermissions.COMMAND_LOBBY)

    literalArgument("reload") {
        withPermission(LobbyPermissions.COMMAND_LOBBY_RELOAD)
        anyExecutor { executor, _ ->
            lobbyConfigHolder.reload()

            executor.sendConfigReloaded()
        }
    }
}
