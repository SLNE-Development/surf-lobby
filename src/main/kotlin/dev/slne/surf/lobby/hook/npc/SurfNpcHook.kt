package dev.slne.surf.lobby.hook.npc

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.LobbyQueue
import dev.slne.surf.lobby.utils.Locations
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.npc.api.dsl.NpcDslBuilder
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import java.util.concurrent.TimeUnit

object SurfNpcHook {
    lateinit var survivalNpc: Npc
    lateinit var eventNpc: Npc

    lateinit var spawnRulesNpc: Npc
    lateinit var spawnSurvivalNpc: Npc
    lateinit var spawnEventNpc: Npc
    lateinit var shopNpc: Npc

    var eventServerDisplayName: String = "Event"

    fun initialize() {
        createSurvivalNpc()
        createEventNpc()
        createSpawnRulesNpc()
        createSpawnSurvivalNpc()
        createSpawnEventNpc()
        createSpawnShopNpc()

        plugin.logger.info("Successfully loaded surf-npc integration.")
    }

    private fun createSurvivalNpc() {
        survivalNpc = npc {
            displayName = {
                note("CastSMP".toSmallCaps()).decorate(TextDecoration.BOLD)
                appendNewline()
                spacer("26.1.2+")
            }
            type = EntityType.MANNEQUIN
            uniqueName = "survival"
            skin = SurfNpcSkins.SURVIVAL.getSkin()

            location = Locations.SURVIVAL_NPC.getLocation()
            rotationType = NpcRotationType.FIXED

            withEventHandler<NpcInteractEvent> {
                LobbyQueue.queueToSurvivalServer(it.player)
            }
            scale(1.5)
        }
    }

    private fun createEventNpc() {
        eventNpc = npc {
            displayName = {
                primary(eventServerDisplayName.toSmallCaps(), TextDecoration.BOLD)
                appendNewline()
                spacer("26.2")
            }
            type = EntityType.MANNEQUIN
            uniqueName = "event"
            skin = SurfNpcSkins.EVENT.getSkin()

            location = Locations.EVENT_NPC.getLocation()

            withEventHandler<NpcInteractEvent> {
                val player = it.player

                if (lobbyConfig.externalEventEnabled && lobbyConfig.externalEventReplacesDefault) {
                    player.teleportAsync(lobbyConfig.externalEventTeleportLocation.toLocation())
                        .thenRun {
                            player.playSound(true) {
                                type(Sound.ENTITY_ENDERMAN_TELEPORT)
                                pitch(1.1f)
                            }
                        }
                    return@withEventHandler
                }

                when (eventServerBridge.state.get()) {
                    EventServerState.OPEN -> {
                        LobbyQueue.queueToEventServer(player)
                        return@withEventHandler
                    }

                    EventServerState.CLOSED -> {
                        if (player.hasPermission(PermissionRegistry.EVENT_BYPASS)) {
                            LobbyQueue.queueToEventServer(player)
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
            scale(1.5)
        }
    }

    private fun createSpawnSurvivalNpc() {
        spawnSurvivalNpc = npc {
            displayName = {
                note("CastSMP".toSmallCaps(), TextDecoration.BOLD)
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
                        pitch(1.5f)
                    }
                }
            }
            rotationType = NpcRotationType.FIXED
            scale(1.5)
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

                if (lobbyConfig.externalEventEnabled && lobbyConfig.externalEventReplacesDefault) {
                    player.teleportAsync(lobbyConfig.externalEventTeleportLocation.toLocation())
                        .thenRun {
                            player.playSound(true) {
                                type(Sound.ENTITY_ENDERMAN_TELEPORT)
                                pitch(1.1f)
                            }
                        }
                    return@withEventHandler
                }

                player.teleportAsync(Locations.EVENT_TELEPORT.getLocation()).thenRun {
                    player.playSound(true) {
                        type(Sound.ENTITY_ENDERMAN_TELEPORT)
                        pitch(1.5f)
                    }
                }
            }
            scale(1.5)
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
            scale(1.5)
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
            scale(1.5)
        }
    }


    private fun NpcDslBuilder.withKickback() = withEventHandler<NpcCollisionEvent> {
        val player = it.player
        player.velocity = player.location.direction.multiply(-1.5)
    }

    private lateinit var syncTask: ScheduledTask

    fun startSyncTask() {
        syncTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, {
            val server =
                SurfCoreApi.getServerByName(lobbyConfig.eventServerName) ?: return@runAtFixedRate

            if (eventServerDisplayName != server.displayName) {
                eventServerDisplayName = server.displayName

                eventNpc.refresh()
            }
        }, 0L, 30L, TimeUnit.SECONDS)
    }

    fun stopSyncTask() {
        if (::syncTask.isInitialized) {
            syncTask.cancel()
        }
    }
}
