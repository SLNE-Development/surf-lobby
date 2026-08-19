package dev.slne.surf.lobby.utils

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.paper.util.surfPlayer
import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.plugin
import dev.slne.surf.queue.api.queue
import org.bukkit.entity.Player

object LobbyQueue {
    fun queueToEventServer(player: Player) {
        SurfCoreApi.getServerByName(lobbyConfig.eventServerName)
            ?.let { server ->
                plugin.launch {
                    if (player.hasPermission(PermissionRegistry.QUEUE_BYPASS)) {
                        player.sendText {
                            appendInfoPrefix()
                            info("Du hast die Warteschlange umgangen und wirst nun mit dem Event Server verbunden...")
                        }
                        val status = SurfCoreApi.sendPlayerAwaiting(player.surfPlayer, server)

                        if (status.isSuccessful()) {
                            player.sendText {
                                appendSuccessPrefix()
                                success("Du wurdest erfolgreich zum Event Server teleportiert.")
                            }
                        } else {
                            player.sendText {
                                appendErrorPrefix()
                                error("Es gab ein Problem beim Teleportieren zum Event Server: ${status.status}")
                            }
                        }

                        return@launch
                    }

                    val success = server.queue().enqueue(player.uniqueId)

                    if (success) {
                        player.sendText {
                            appendSuccessPrefix()
                            success("Du wurdest in die Warteschlange für den Event Server eingereiht.")
                        }
                    } else {
                        player.sendText {
                            appendErrorPrefix()
                            error("Du bist bereits in einer Warteschlange!")
                        }
                    }
                }
            }
    }

    fun queueToSurvivalServer(player: Player) {
        if (!lobbyConfig.survivalOpen && !player.hasPermission(PermissionRegistry.SURVIVAL_BYPASS)) {
            player.sendText {
                spacer("[")
                note("Nepomuk")
                spacer("]")
                appendSpace()
                error("Der Survival Server startet bald, sei bereit!")
            }
            return
        }


        SurfCoreApi.getServerByName(lobbyConfig.survivalServerName)
            ?.let { server ->
                plugin.launch {
                    if (player.hasPermission(PermissionRegistry.QUEUE_BYPASS)) {
                        player.sendText {
                            appendInfoPrefix()
                            info("Du hast die Warteschlange umgangen und wirst nun mit dem Survival Server verbunden...")
                        }
                        val status = SurfCoreApi.sendPlayerAwaiting(player.surfPlayer, server)

                        if (status.isSuccessful()) {
                            player.sendText {
                                appendSuccessPrefix()
                                success("Du wurdest erfolgreich zum Survival Server teleportiert.")
                            }
                        } else {
                            player.sendText {
                                appendErrorPrefix()
                                error("Es gab ein Problem beim Teleportieren zum Survival Server: ${status.status}")
                                status.velocityMessage?.let {
                                    error(": ")
                                    append(it)
                                }
                            }
                        }

                        return@launch
                    }

                    val success = server.queue().enqueue(player.uniqueId)

                    if (success) {
                        player.sendText {
                            appendSuccessPrefix()
                            success("Du wurdest in die Warteschlange für den Survival Server eingereiht.")
                        }
                    } else {
                        player.sendText {
                            appendErrorPrefix()
                            error("Du bist bereits in einer Warteschlange!")
                        }
                    }
                }
            }
    }
}