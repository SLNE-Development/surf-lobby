package dev.slne.surf.lobby.npc

import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.plugin
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.npc.Npc
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.npc.api.result.NpcCreationResult
import dev.slne.surf.npc.api.surfNpcApi
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import net.kyori.adventure.text.format.TextDecoration

object SurfNpcHook {
    lateinit var survivalNpc: Npc
    lateinit var eventNpc: Npc

    fun initialize() {
        createSurvivalNpc()
        createEventNpc()

        plugin.logger.info("Successfully loaded surf-npc integration.")
    }

    fun reload() {
        surfNpcApi.deleteNpc(survivalNpc)
        surfNpcApi.deleteNpc(eventNpc)

        createSurvivalNpc()
        createEventNpc()
    }

    private fun createSurvivalNpc() {
        survivalNpc = npc(plugin) {
            displayName = {
                primary("Nepomuk".toSmallCaps(), TextDecoration.BOLD)
            }
            uniqueName = "survival"
            skin = SurfNpcSkins.SURVIVAL.getSkin()

            location {
                world = lobbyConfig.survivalNpc.world
                x = lobbyConfig.survivalNpc.x
                y = lobbyConfig.survivalNpc.y
                z = lobbyConfig.survivalNpc.z
            }

            fixedRotation = surfNpcApi.createRotation(
                lobbyConfig.survivalNpc.yaw,
                lobbyConfig.survivalNpc.pitch
            )
            rotationType = NpcRotationType.FIXED
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
            skin = SurfNpcSkins.EVENT.getSkin()

            location {
                world = lobbyConfig.eventNpc.world
                x = lobbyConfig.eventNpc.x
                y = lobbyConfig.eventNpc.y
                z = lobbyConfig.eventNpc.z
            }

            fixedRotation =
                surfNpcApi.createRotation(lobbyConfig.eventNpc.yaw, lobbyConfig.eventNpc.pitch)
            rotationType = NpcRotationType.FIXED
        }.getOrNull() ?: error("Failed to create survival NPC")
    }

    private fun NpcCreationResult.getOrNull() = when (this) {
        is NpcCreationResult.Success -> this.npc
        is NpcCreationResult.Failure -> null
    }
}