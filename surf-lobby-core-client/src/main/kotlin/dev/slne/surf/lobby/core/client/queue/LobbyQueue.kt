package dev.slne.surf.lobby.core.client.queue

import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.queue.api.queue
import java.util.*

object LobbyQueue {
    fun queueToEventServer(playerUuid: UUID) {
        queueToServer(
            playerUuid,
            lobbyConfig.eventServerName,
            "Event Server",
            includeVelocityMessage = false
        )
    }

    fun queueToSurvivalServer(playerUuid: UUID) {
        if (!lobbyConfig.survivalOpen
            && !LobbyPlatform.hasPermission(playerUuid, LobbyPermissions.SURVIVAL_BYPASS)
        ) {
            LobbyPlatform.audience(playerUuid)?.sendText {
                spacer("[")
                note("Nepomuk")
                spacer("]")
                appendSpace()
                error("Der Survival Server startet bald, sei bereit!")
            }
            return
        }

        queueToServer(
            playerUuid,
            lobbyConfig.survivalServerName,
            "Survival Server",
            includeVelocityMessage = true
        )
    }

    private fun queueToServer(
        playerUuid: UUID,
        serverName: String,
        serverLabel: String,
        includeVelocityMessage: Boolean
    ) {
        SurfCoreApi.getServerByName(serverName)
            ?.let { server ->
                LobbyPlatform.launch {
                    if (LobbyPlatform.hasPermission(playerUuid, LobbyPermissions.QUEUE_BYPASS)) {
                        LobbyPlatform.audience(playerUuid)?.sendText {
                            appendInfoPrefix()
                            info("Du hast die Warteschlange umgangen und wirst nun mit dem $serverLabel verbunden...")
                        }
                        val surfPlayer = SurfCoreApi.getPlayer(playerUuid)
                            ?: error("SurfPlayer for player $playerUuid not found!")
                        val status = SurfCoreApi.sendPlayerAwaiting(surfPlayer, server)

                        if (status.isSuccessful()) {
                            LobbyPlatform.audience(playerUuid)?.sendText {
                                appendSuccessPrefix()
                                success("Du wurdest erfolgreich zum $serverLabel teleportiert.")
                            }
                        } else {
                            LobbyPlatform.audience(playerUuid)?.sendText {
                                appendErrorPrefix()
                                error("Es gab ein Problem beim Teleportieren zum $serverLabel: ${status.status}")
                                if (includeVelocityMessage) {
                                    status.velocityMessage?.let {
                                        error(": ")
                                        append(it)
                                    }
                                }
                            }
                        }

                        return@launch
                    }

                    val success = server.queue().enqueue(playerUuid)

                    if (success) {
                        LobbyPlatform.audience(playerUuid)?.sendText {
                            appendSuccessPrefix()
                            success("Du wurdest in die Warteschlange für den $serverLabel eingereiht.")
                        }
                    } else {
                        LobbyPlatform.audience(playerUuid)?.sendText {
                            appendErrorPrefix()
                            error("Du bist bereits in einer Warteschlange!")
                        }
                    }
                }
            }
    }
}
