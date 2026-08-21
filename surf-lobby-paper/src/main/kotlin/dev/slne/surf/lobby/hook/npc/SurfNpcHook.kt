package dev.slne.surf.lobby.hook.npc

import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.core.util.objectSetOf
import dev.slne.surf.lobby.config.toLocation
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.event.EventJoinAction
import dev.slne.surf.lobby.core.client.event.EventServerDisplayName
import dev.slne.surf.lobby.core.client.event.decideEventJoin
import dev.slne.surf.lobby.core.client.location.LobbyLocations
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendEventServerClosed
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendNoEventRunning
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendNpcRules
import dev.slne.surf.lobby.core.client.npc.LobbyNpcContents
import dev.slne.surf.lobby.core.client.npc.LobbyNpcSkins
import dev.slne.surf.lobby.core.client.queue.LobbyQueue
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.getLocation
import dev.slne.surf.npc.api.dsl.NpcDslBuilder
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.event.NpcCollisionEvent
import dev.slne.surf.npc.api.event.NpcInteractEvent
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.npc.skin.NpcSkin
import dev.slne.surf.npc.api.npc.skin.NpcSkinPart
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
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
                append(LobbyNpcContents.survivalNpcName)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "survival"
            skin = LobbyNpcSkins.SURVIVAL.toNpcSkin()

            location = LobbyLocations.SURVIVAL_NPC.getLocation()
            rotationType = NpcRotationType.FIXED

            withEventHandler<NpcInteractEvent> {
                LobbyQueue.queueToSurvivalServer(it.player.uniqueId)
            }
            scale(1.5)
        }
    }

    private fun createEventNpc() {
        eventNpc = npc {
            displayName = {
                append(LobbyNpcContents.eventNpcName(EventServerDisplayName.current))
            }
            type = EntityType.MANNEQUIN
            uniqueName = "event"
            skin = LobbyNpcSkins.EVENT.toNpcSkin()

            location = LobbyLocations.EVENT_NPC.getLocation()

            withEventHandler<NpcInteractEvent> {
                val player = it.player

                when (decideEventJoin(player.uniqueId)) {
                    EventJoinAction.EXTERNAL_TELEPORT -> {
                        player.teleportAsync(lobbyConfig.externalEventTeleportLocation.toLocation())
                            .thenRun {
                                player.playSound(true) {
                                    type(Sound.ENTITY_ENDERMAN_TELEPORT)
                                    pitch(1.1f)
                                }
                            }
                    }

                    EventJoinAction.QUEUE -> LobbyQueue.queueToEventServer(player.uniqueId)

                    EventJoinAction.CLOSED_MESSAGE -> player.sendEventServerClosed()

                    EventJoinAction.NO_EVENT_MESSAGE -> player.sendNoEventRunning()
                }
            }

            rotationType = NpcRotationType.FIXED
            scale(1.5)
        }
    }

    private fun createSpawnSurvivalNpc() {
        spawnSurvivalNpc = npc {
            displayName = {
                append(LobbyNpcContents.spawnSurvivalNpcName)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "spawn_survival"
            skin = LobbyNpcSkins.SURVIVAL.toNpcSkin()

            location = LobbyLocations.SPAWN_SURVIVAL.getLocation()
            withKickback()
            withEventHandler<NpcInteractEvent> {
                val player = it.player

                player.teleportAsync(LobbyLocations.SURVIVAL_TELEPORT.getLocation()).thenRun {
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
                append(LobbyNpcContents.spawnEventNpcName)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "spawn_event"
            skin = LobbyNpcSkins.EVENT.toNpcSkin()

            location = LobbyLocations.SPAWN_EVENT.getLocation()

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

                player.teleportAsync(LobbyLocations.EVENT_TELEPORT.getLocation()).thenRun {
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
                append(LobbyNpcContents.shopNpcName)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "shop"
            skin = LobbyNpcSkins.UNKNOWN.toNpcSkin()

            location = LobbyLocations.SPAWN_SHOP.getLocation()
            withKickback()
            rotationType = NpcRotationType.FIXED
            scale(1.5)
        }
    }

    private fun createSpawnRulesNpc() {
        spawnRulesNpc = npc {
            displayName = {
                append(LobbyNpcContents.rulesNpcName)
            }
            type = EntityType.MANNEQUIN
            uniqueName = "spawn_rules"
            skin = LobbyNpcSkins.RULES.toNpcSkin()

            location = LobbyLocations.SPAWN_RULES.getLocation()
            withKickback()
            withEventHandler<NpcInteractEvent> {
                it.player.sendNpcRules()
            }
            rotationType = NpcRotationType.FIXED
            scale(1.5)
        }
    }


    private fun NpcDslBuilder.withKickback() = withEventHandler<NpcCollisionEvent> {
        val player = it.player
        player.velocity = player.location.direction.multiply(-1.5)
    }

    private fun LobbyNpcSkins.toNpcSkin() =
        NpcSkin(name, skinValue, skinSignature, objectSetOf(*NpcSkinPart.entries.toTypedArray()))

    private lateinit var syncTask: ScheduledTask

    fun startSyncTask() {
        syncTask = Bukkit.getAsyncScheduler().runAtFixedRate(plugin, {
            if (EventServerDisplayName.refresh() != null) {
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
