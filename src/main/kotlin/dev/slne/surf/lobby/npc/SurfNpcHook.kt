package dev.slne.surf.lobby.npc

import dev.slne.surf.lobby.plugin
import dev.slne.surf.npc.api.dsl.npc
import dev.slne.surf.npc.api.npc.rotation.NpcRotationType
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit

object SurfNpcHook {
    fun initialize() {
        createSurvivalNpc()
        createEventNpc()

        plugin.logger.info("Successfully loaded surf-npc integration.")
    }

    private fun createSurvivalNpc() {
        npc(plugin) {
            displayName = {
                primary("survival".toSmallCaps(), TextDecoration.BOLD)
                appendNewline()
                spacer("(Survival - 1.21.11)")
            }
            uniqueName = "survival"
            skin = SurfNpcSkins.SURVIVAL.getSkin()

            location {
                world = Bukkit.getWorlds().first().name
                x = 0.5
                y = 100.0
                z = 0.5
            }

            rotationType = NpcRotationType.FIXED
        }
    }

    private fun createEventNpc() {
        npc(plugin) {
            displayName = {
                primary("event".toSmallCaps(), TextDecoration.BOLD)
                appendNewline()
                spacer("(Adventure - 1.21.11)")
            }
            uniqueName = "event"
            skin = SurfNpcSkins.EVENT.getSkin()

            location {
                world = Bukkit.getWorlds().first().name
                x = 0.5
                y = 100.0
                z = 0.5
            }

            rotationType = NpcRotationType.FIXED
        }
    }
}