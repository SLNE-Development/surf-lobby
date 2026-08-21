package dev.slne.surf.lobby.minestom.command

import dev.slne.minestom.lobby.api.command.commandapi.dsl.anyExecutor
import dev.slne.minestom.lobby.api.command.commandapi.dsl.commandTree
import dev.slne.minestom.lobby.api.command.commandapi.dsl.literalArgument
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
