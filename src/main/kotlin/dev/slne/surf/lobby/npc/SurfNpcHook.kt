package dev.slne.surf.lobby.npc

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
import dev.slne.surf.surfapi.bukkit.api.surfBukkitApi
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.clickOpensUrl
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound

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
        survivalNpc = npc(plugin) {
            displayName = {
                note("Nepomuk".toSmallCaps()).decorate(TextDecoration.BOLD)
            }
            uniqueName = "survival"
            skin = SurfNpcSkins.UNKNOWN.getSkin()

            location = Locations.SURVIVAL_NPC.getLocation().toNpcLocation()
            fixedRotation = Locations.SURVIVAL_NPC.getLocation().toNpcRotation()
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
        }.getOrNull() ?: error("Failed to create survival NPC")
    }

    private fun createEventNpc() {
        eventNpc = npc(plugin) {
            displayName = {
                primary("event".toSmallCaps(), TextDecoration.BOLD)
                appendNewline()
                spacer("(Adventure - 1.21.11)")
            }
            uniqueName = "event"
            skin = SurfNpcSkins.UNKNOWN.getSkin()

            location = Locations.EVENT_NPC.getLocation().toNpcLocation()
            fixedRotation = Locations.EVENT_NPC.getLocation().toNpcRotation()

            withEventHandler<NpcInteractEvent> {
                val player = it.player

                when (eventServerBridge.state.get()) {
                    EventServerState.OPEN -> {
                        surfBukkitApi.sendPlayerToServer(player, lobbyConfig.eventServerName)
                        return@withEventHandler
                    }

                    EventServerState.CLOSED -> {
                        if (player.hasPermission(PermissionRegistry.EVENT_BYPASS)) {
                            surfBukkitApi.sendPlayerToServer(player, lobbyConfig.eventServerName)
                            return@withEventHandler
                        }
                        player.sendText {
                            appendPrefix()
                            error("Der Event Server ist aktuell geschlossen!")
                        }
                    }

                    EventServerState.UNKNOWN -> {
                        player.sendText {
                            appendPrefix()
                            error("Aktuell findet kein Event statt!")
                        }
                    }
                }
            }

            rotationType = NpcRotationType.FIXED
        }.getOrNull() ?: error("Failed to create survival NPC")
    }

    private fun createSpawnSurvivalNpc() {
        spawnSurvivalNpc = npc(plugin) {
            displayName = {
                note("Survival".toSmallCaps(), TextDecoration.BOLD)
            }
            uniqueName = "spawn_survival"
            skin = SurfNpcSkins.UNKNOWN.getSkin()

            location {
                world = Bukkit.getWorlds().first().name
                x = 95.5
                y = 149.0
                z = 319.5
            }
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
            fixedRotation = surfNpcApi.createRotation(-120f, 0f)
            rotationType = NpcRotationType.FIXED
        }.getOrNull() ?: error("Failed to create spawn survival NPC")
    }

    private fun createSpawnEventNpc() {
        spawnEventNpc = npc(plugin) {
            displayName = {
                note("Event".toSmallCaps(), TextDecoration.BOLD)
            }
            uniqueName = "spawn_event"
            skin = SurfNpcSkins.UNKNOWN.getSkin()

            location {
                world = Bukkit.getWorlds().first().name
                x = 95.5
                y = 149.0
                z = 311.5
            }
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
            fixedRotation = surfNpcApi.createRotation(-60f, 0f)
            rotationType = NpcRotationType.FIXED
        }.getOrNull() ?: error("Failed to create spawn event NPC")
    }

    private fun createSpawnShopNpc() {
        shopNpc = npc(plugin) {
            displayName = {
                note("Shop".toSmallCaps(), TextDecoration.BOLD, TextDecoration.OBFUSCATED)
            }
            uniqueName = "shop"
            skin = SurfNpcSkins.UNKNOWN.getSkin()

            location {
                world = Bukkit.getWorlds().first().name
                x = 97.5
                y = 149.0
                z = 307.5
            }
            withKickback()
            fixedRotation = surfNpcApi.createRotation(-37f, 0f)
            rotationType = NpcRotationType.FIXED
        }.getOrNull() ?: error("Failed to create spawn shop NPC")
    }

    private fun createSpawnRulesNpc() {
        spawnRulesNpc = npc(plugin) {
            displayName = {
                note("Regelwerk".toSmallCaps(), TextDecoration.BOLD)
            }
            uniqueName = "spawn_rules"
            skin = SurfNpcSkins.UNKNOWN.getSkin()

            location {
                world = Bukkit.getWorlds().first().name
                x = 97.5
                y = 149.0
                z = 323.5
            }
            withKickback()
            withEventHandler<NpcInteractEvent> {
                it.player.sendText {
                    appendPrefix()
                    primary("Das Regelwerk findest du hier: ")
                    variableValue("server.castcrafter.de/rules")
                    clickOpensUrl("https://server.castcrafter.de/rules")
                }
            }
            fixedRotation = surfNpcApi.createRotation(-143f, 0f)
            rotationType = NpcRotationType.FIXED
        }.getOrNull() ?: error("Failed to create spawn rules NPC")
    }

    private fun NpcCreationResult.getOrNull() = when (this) {
        is NpcCreationResult.Success -> this.npc
        is NpcCreationResult.Failure -> null
    }


    private fun NpcDslBuilder.withKickback() = withEventHandler<NpcCollisionEvent> {
        val player = it.player
        player.velocity = player.location.direction.multiply(-1.5)
    }

    private fun Location.toNpcLocation() = surfNpcApi.createLocation(
        x,
        y,
        z,
        world.name
    )

    private fun Location.toNpcRotation() = surfNpcApi.createRotation(
        yaw,
        pitch
    )
}