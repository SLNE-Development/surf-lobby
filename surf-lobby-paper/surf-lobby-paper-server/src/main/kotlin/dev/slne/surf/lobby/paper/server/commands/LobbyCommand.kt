package dev.slne.surf.lobby.paper.server.commands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.surf.cloud.api.common.player.toCloudPlayer
import dev.slne.surf.lobby.paper.common.utils.PermissionRegistry
import dev.slne.surf.lobby.paper.server.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

fun lobbyCommand() = commandAPICommand("lobby") {
    withAliases("hub", "ichbineinstarholmichhierraus")
    withPermission(PermissionRegistry.LOBBY)

    playerExecutor { player, _ ->
        val cloudPlayer = player.toCloudPlayer() ?: error("Cloud player is null for lobby command")

        plugin.launch {
            val result = cloudPlayer.connectToServer("lobby")

            if (!result.isSuccess) {
                player.sendText {
                    appendPrefix()

                    error("Du konntest nicht auf den Lobby-Server verbunden werden. Bitte versuche es später erneut. Grund: ")
                    append(result.message)
                    error(".")
                }
            }
        }
    }
}