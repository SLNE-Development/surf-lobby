package dev.slne.surf.lobby.hook.npc

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.core.api.common.surfCoreApi
import dev.slne.surf.core.api.paper.util.surfPlayer
import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.Locations
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.npc.api.dsl.NpcDslBuilder
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.queue.api.queue
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.clickOpensUrl
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

object SurfNpcHook {
    lateinit var survivalNpc: Npc
    lateinit var eventNpc: Npc

    lateinit var spawnRulesNpc: Npc
    lateinit var spawnSurvivalNpc: Npc
    lateinit var spawnEventNpc: Npc
    lateinit var shopNpc: Npc

    fun initialize() {
        createSurvivalNpc()
        createEventNpc()
        createSpawnRulesNpc()
        createSpawnSurvivalNpc()
        createSpawnEventNpc()
        createSpawnShopNpc()

        plugin.logger.info("Successfully loaded surf-npc integration.")
    }

    fun reload() {
        surfNpcApi.deleteNpc(survivalNpc)
        surfNpcApi.deleteNpc(eventNpc)
        surfNpcApi.deleteNpc(spawnRulesNpc)
        surfNpcApi.deleteNpc(spawnSurvivalNpc)
        surfNpcApi.deleteNpc(spawnEventNpc)
        surfNpcApi.deleteNpc(shopNpc)

        createSurvivalNpc()
        createEventNpc()
        createSpawnRulesNpc()
        createSpawnSurvivalNpc()
        createSpawnEventNpc()
        createSpawnShopNpc()
    }

    private fun createSurvivalNpc() {
        survivalNpc = npc {
            displayName = {
                note("Nepomuk".toSmallCaps()).decorate(TextDecoration.BOLD)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "survival"
            skin = SurfNpcSkins.SURVIVAL.getSkin()

            location = Locations.SURVIVAL_NPC.getLocation()
            rotationType = NpcRotationType.FIXED

            withEventHandler<NpcInteractEvent> {
                it.player.sendText {
                    spacer("[")
                    note("Nepomuk")
                    spacer("]")
                    appendSpace()
                    error("Ich konnte noch keine Verbindung zum Planeten \"Survival\" herstellen...")
                }
                it.player.playSound(true) {
                    type(Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT)
                }
            }
        }
    }

    private val eventServerDisplayName by lazy {
        val server = surfCoreApi.getServerByName(lobbyConfig.eventServerName)
            ?: error("Event server with name ${lobbyConfig.eventServerName} not found")

        server.displayName
    }

    private fun createEventNpc() {
        eventNpc = npc {
            displayName = {
                primary(eventServerDisplayName.toSmallCaps(), TextDecoration.BOLD)
                appendNewline()
                spacer("(Event - 1.21.11)")
            }
            type = EntityType.MANNEQUIN
            uniqueName = "event"
            skin = SurfNpcSkins.EVENT.getSkin()

            location = Locations.EVENT_NPC.getLocation()

            withEventHandler<NpcInteractEvent> {
                val player = it.player

                when (eventServerBridge.state.get()) {
                    EventServerState.OPEN -> {
                        queueToEventServer(player)
                        return@withEventHandler
                    }

                    EventServerState.CLOSED -> {
                        if (player.hasPermission(PermissionRegistry.EVENT_BYPASS)) {
                            queueToEventServer(player)
                            return@withEventHandler
                        }
                        player.sendText {
                            appendErrorPrefix()
                            error("Der Event Server ist aktuell geschlossen!")
                        }
                    }

                    EventServerState.UNKNOWN -> {
                        player.sendText {
                            appendErrorPrefix()
                            error("Aktuell findet kein Event statt!")
                        }
                    }
                }
            }

            rotationType = NpcRotationType.FIXED
        }
    }

    private fun createSpawnSurvivalNpc() {
        spawnSurvivalNpc = npc {
            displayName = {
                note("Survival".toSmallCaps(), TextDecoration.BOLD)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "spawn_survival"
            skin = SurfNpcSkins.SURVIVAL.getSkin()

            location = Locations.SPAWN_SURVIVAL.getLocation()
            withKickback()
            withEventHandler<NpcInteractEvent> {
                val player = it.player

                player.teleportAsync(Locations.SURVIVAL_TELEPORT.getLocation()).thenRun {
                    player.playSound(true) {
                        type(Sound.ENTITY_ENDERMAN_TELEPORT)
                        pitch(2.0f)
                    }
                }
            }
            rotationType = NpcRotationType.FIXED
        }
    }

    private fun createSpawnEventNpc() {
        spawnEventNpc = npc {
            displayName = {
                note("Event".toSmallCaps(), TextDecoration.BOLD)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "spawn_event"
            skin = SurfNpcSkins.EVENT.getSkin()

            location = Locations.SPAWN_EVENT.getLocation()
            withKickback()
            withEventHandler<NpcInteractEvent> {
                val player = it.player

                player.teleportAsync(Locations.EVENT_TELEPORT.getLocation()).thenRun {
                    player.playSound(true) {
                        type(Sound.ENTITY_ENDERMAN_TELEPORT)
                        pitch(2.0f)
                    }
                }
            }
            rotationType = NpcRotationType.FIXED
        }
    }

    private fun createSpawnShopNpc() {
        shopNpc = npc {
            displayName = {
                note("Shop".toSmallCaps(), TextDecoration.BOLD, TextDecoration.OBFUSCATED)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "shop"
            skin = SurfNpcSkins.UNKNOWN.getSkin()

            location = Locations.SPAWN_SHOP.getLocation()
            withKickback()
            rotationType = NpcRotationType.FIXED
        }
    }

    private fun createSpawnRulesNpc() {
        spawnRulesNpc = npc {
            displayName = {
                note("Regelwerk".toSmallCaps(), TextDecoration.BOLD)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "spawn_rules"
            skin = SurfNpcSkins.RULES.getSkin()

            location = Locations.SPAWN_RULES.getLocation()
            withKickback()
            withEventHandler<NpcInteractEvent> {
                it.player.sendText {
                    appendInfoPrefix()
                    primary("Das Regelwerk findest du hier: ")
                    variableValue("server.castcrafter.de/rules")
                    clickOpensUrl("https://server.castcrafter.de/rules")
                }
            }
            rotationType = NpcRotationType.FIXED
        }
    }

    private fun NpcCreationResult.getOrNull() = when (this) {
        is NpcCreationResult.Success -> this.npc
        is NpcCreationResult.Failure -> null
    }


    private fun NpcDslBuilder.withKickback() = withEventHandler<NpcCollisionEvent> {
        val player = it.player
        player.velocity = player.location.direction.multiply(-1.5)
    }

    private fun queueToEventServer(player: Player) {
        surfCoreApi.getServerByName(lobbyConfig.eventServerName)
            ?.let { server ->
                plugin.launch {
                    if (player.hasPermission(PermissionRegistry.QUEUE_BYPASS)) {
                        player.sendText {
                            appendInfoPrefix()
                            info("Du hast die Warteschlange umgangen und wirst nun mit dem Event Server verbunden...")
                        }
                        val status = surfCoreApi.sendPlayerAwaiting(player.surfPlayer, server)

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
            } ?: error("Event server with name ${lobbyConfig.eventServerName} not found")
    }
}