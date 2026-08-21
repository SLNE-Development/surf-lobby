package dev.slne.surf.lobby.core.client.npc

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.buildText
import net.kyori.adventure.text.format.TextDecoration

/**
 * The display names of the lobby NPCs, shared between the platforms.
 */
object LobbyNpcContents {

    val survivalNpcName = buildText {
        note("CastSMP".toSmallCaps()).decorate(TextDecoration.BOLD)
        appendNewline()
        spacer("26.2")
    }

    fun eventNpcName(eventServerDisplayName: String) = buildText {
        primary(eventServerDisplayName.toSmallCaps(), TextDecoration.BOLD)
        appendNewline()
        spacer("26.2")
    }

    val spawnSurvivalNpcName = buildText {
        note("CastSMP".toSmallCaps(), TextDecoration.BOLD)
    }

    val spawnEventNpcName = buildText {
        note("Event".toSmallCaps(), TextDecoration.BOLD)
    }

    val shopNpcName = buildText {
        note("Shop".toSmallCaps(), TextDecoration.BOLD, TextDecoration.OBFUSCATED)
    }

    val rulesNpcName = buildText {
        note("Regelwerk".toSmallCaps(), TextDecoration.BOLD)
    }
}
