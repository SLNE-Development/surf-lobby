package dev.slne.surf.lobby.hologram

import dev.slne.surf.hologram.api.hologram.Hologram

object SurfHologramHook {
    lateinit var survivalInfoHologram: Hologram

    fun initialize() {
//        survivalInfoHologram = hologram<SimpleHologram, SimpleHologramOptions>(
//            "survival-info",
//            SimpleHologram::class.java,
//            HologramOrientationType.FIXED,
//            lobbyConfig.survivalInfoHologram.toLocation().toHologramLocation()
//        ) {
//            displayedText {
//                primary("Survival Server".toSmallCaps(), TextDecoration.BOLD)
//                appendNewline(2)
//                note("Erkunde die Welt, baue deine Träume und eröffne dein eigenes Business!".toSmallCaps())
//            }
//
//            options {
//
//            }
//        }
//
//        survivalInfoHologram.show()
    }

    fun shutdown() {
//        surfHologramApi.deleteHologram(survivalInfoHologram)
    }

    fun reload() {
        shutdown()
        initialize()
    }
}