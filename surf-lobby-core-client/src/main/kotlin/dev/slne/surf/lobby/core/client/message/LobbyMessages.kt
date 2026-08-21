package dev.slne.surf.lobby.core.client.message

import dev.slne.surf.api.core.messages.adventure.sendText
import net.kyori.adventure.audience.Audience

/**
 * The chat messages the lobby sends, shared between the platforms.
 */
object LobbyMessages {

    fun Audience.sendConfigReloaded() = sendText {
        appendSuccessPrefix()
        success("Lobby configuration reloaded successfully.")
    }

    fun Audience.sendSpawnTeleported() = sendText {
        appendSuccessPrefix()
        success("Du wurdest zum Spawn teleportiert.")
    }

    fun Audience.sendNavigatorSpawnTeleported() = sendText {
        appendInfoPrefix()
        info("Du wurdest zum Spawn teleportiert.")
    }

    fun Audience.sendEventServerClosed() = sendText {
        appendErrorPrefix()
        error("Der Event Server ist aktuell geschlossen!")
    }

    fun Audience.sendNoEventRunning() = sendText {
        appendErrorPrefix()
        error("Aktuell findet kein Event statt!")
    }

    fun Audience.sendNpcRules() = sendText {
        appendInfoPrefix()
        primary("Das Regelwerk findest du hier: ")
        variableValue("server.castcrafter.de/rules")
        clickOpensUrl("https://server.castcrafter.de/rules")
    }

    fun Audience.sendNavigatorRules() = sendText {
        appendInfoPrefix()
        info("Das Regelwerk findest du hier: ")
        append {
            variableValue("docs.castcrafter.de/rules")
            clickOpensUrl("https://docs.castcrafter.de/rules")
        }
    }

    fun Audience.sendPushbackEnabled() = sendText {
        appendInfoPrefix()
        info("Du hast den Pushback ")
        success("aktiviert.")
    }

    fun Audience.sendPushbackDisabled() = sendText {
        appendInfoPrefix()
        info("Du hast den Pushback ")
        error("deaktiviert.")
    }

    fun Audience.sendVisibilityShowAll() = sendText {
        appendInfoPrefix()
        info("Du siehst jetzt ")
        success("alle Spieler")
        info(".")
    }

    fun Audience.sendVisibilityShowTeam() = sendText {
        appendInfoPrefix()
        info("Du siehst jetzt nur noch ")
        yellow("Teammitglieder")
        info(".")
    }

    fun Audience.sendVisibilityShowNone() = sendText {
        appendInfoPrefix()
        info("Du siehst jetzt ")
        error("keine Spieler")
        info(".")
    }

    fun Audience.sendAlreadyConnecting() = sendText {
        appendInfoPrefix()
        error("Du verbindest dich bereits mit einem Server...")
    }

    fun Audience.sendServerUnreachable() = sendText {
        appendInfoPrefix()
        error("Dieser Server ist derzeit nicht erreichbar!")
    }
}
