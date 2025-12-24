package dev.slne.surf.lobby.hologram

import dev.slne.surf.hologram.api.dsl.hologram
import dev.slne.surf.hologram.api.hologram.Hologram
import dev.slne.surf.hologram.api.hologram.SimpleHologram
import dev.slne.surf.hologram.api.hologram.type.SimpleHologramOptions
import dev.slne.surf.hologram.api.hologram.util.HologramOrientationType
import dev.slne.surf.hologram.api.surfHologramApi
import dev.slne.surf.hologram.api.util.show
import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.utils.note
import dev.slne.surf.lobby.utils.toHologramLocation
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import net.kyori.adventure.text.format.TextDecoration

object SurfHologramHook {
    lateinit var survivalInfoHologram: Hologram
    fun initialize() {
        survivalInfoHologram = hologram<SimpleHologram, SimpleHologramOptions>(
            "survival-info",
            SimpleHologram::class.java,
            HologramOrientationType.FIXED,
            lobbyConfig.survivalInfoHologram.toLocation().toHologramLocation()
        ) {
            displayedText {
                primary("Survival Server".toSmallCaps(), TextDecoration.BOLD)
                appendNewline(2)
                note("Erkunde die Welt, baue deine Träume und".toSmallCaps())
                appendNewline()
                note("eröffne dein eigenes Business!".toSmallCaps())
            }

            options {

            }
        }

        survivalInfoHologram.show()
    }

    fun shutdown() {
        surfHologramApi.deleteHologram(survivalInfoHologram)
    }

    fun reload() {
        shutdown()
        initialize()
    }
}