package dev.slne.surf.lobby.command

import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.lobby.lobbyConfigHolder
import dev.slne.surf.lobby.utils.PermissionRegistry

fun lobbyCommand() = commandTree("surflobby") {
    withPermission(PermissionRegistry.COMMAND_LOBBY)

    literalArgument("reload") {
        withPermission(PermissionRegistry.COMMAND_LOBBY_RELOAD)
        anyExecutor { executor, _ ->
            lobbyConfigHolder.reload()

            executor.sendText {
                appendSuccessPrefix()
                success("Lobby configuration reloaded successfully.")
            }
        }
    }
}