package dev.slne.surf.lobby.command

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.slne.surf.lobby.config.LobbyConfig
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun lobbyCommand() = commandTree("lobby") {
    withPermission(PermissionRegistry.COMMAND_LOBBY)

    literalArgument("reload") {
        withPermission(PermissionRegistry.COMMAND_LOBBY_RELOAD)
        anyExecutor { executor, _ ->
            LobbyConfig.reloadFromFile()

            executor.sendText {
                appendPrefix()
                success("Lobby configuration reloaded successfully.")
            }
        }
    }
}