package dev.slne.surf.lobby.core.client.menu

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.server.SurfServer
import dev.slne.surf.core.api.common.server.state.SurfServerState
import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.event.eventServerBridge
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.empty

/**
 * The names and lore lines of the navigator and lobby selector menus, shared between the
 * platforms.
 */
object NavigatorContents {

    val lobbySelectorName = buildText { primary("Lobby Auswahl") }
    val spawnName = buildText { primary("Spawn") }
    val rulesName = buildText { primary("Regelwerk") }
    val cosmeticsName = buildText { primary("???") }

    val survivalServerName = buildText {
        primary("CastSMP ")
        darkSpacer("(Survival)")
    }

    val eventServerName = buildText { primary("Event Server") }

    val noLobbyAvailableName = buildText { error("Keine Lobby Server verfügbar") }

    fun survivalServerLore(): List<Component> = buildList {
        val survivalSurfServer = SurfCoreApi.getServerByName(lobbyConfig.survivalServerName)
        val survivalState = survivalSurfServer?.state

        add(empty())
        add(buildText { note("Beschreibung:".toSmallCaps()) })
        add(buildText {
            info("Der ")
            variableValue("CastSMP")
            info(" ist ein")
            variableValue(" friedlicher")
            info(" Survival Server.")
        })
        add(buildText {
            info("Hier kannst du entspannt")
            variableValue(" deine Träume")
            info(" verwirklichen.")
        })

        add(empty())

        add(buildText { note("Status:".toSmallCaps()) })
        add(buildText {
            when (survivalState) {
                SurfServerState.RUNNING -> success("Der CastSMP ist erreichbar")
                else -> error("Der CastSMP ist derzeit nicht erreichbar")
            }
        })

        if (survivalState == SurfServerState.RUNNING) {
            add(empty())
            add(buildText { note("Spieler:".toSmallCaps()) })
            add(buildText {
                val playerCount = survivalSurfServer.getPlayerCount()
                val maxPlayers = survivalSurfServer.maxPlayers
                info("$playerCount / $maxPlayers Spieler online")
            })
        }
    }

    fun eventServerLore(): List<Component> = buildList {
        val eventSurfServer = SurfCoreApi.getServerByName(lobbyConfig.eventServerName)
        val eventState = eventServerBridge.state.get()

        add(empty())
        add(buildText { note("Status:".toSmallCaps()) })
        add(buildText {
            when (eventState) {
                EventServerState.OPEN -> success("Klicke, um dem Event Server beizutreten")
                EventServerState.CLOSED -> error("Der Event Server ist aktuell geschlossen")
                EventServerState.UNKNOWN -> error("Aktuell findet kein Event statt")
            }
        })

        if (eventState != EventServerState.UNKNOWN) {
            add(empty())
            add(buildText { note("Spieler:".toSmallCaps()) })
            add(buildText {
                val playerCount = eventSurfServer?.getPlayerCount() ?: -1
                val maxPlayers = eventSurfServer?.maxPlayers ?: -1
                info("$playerCount / $maxPlayers Spieler online")
            })
        }
    }

    fun lobbyServerName(server: SurfServer) = buildText { variableValue(server.displayName) }

    fun lobbyServerLore(server: SurfServer): List<Component> = buildList {
        val state = server.state

        add(empty())
        add(buildText {
            spacer("-")
            appendSpace()
            note("Status: ")
            variableValue(
                if (state == SurfServerState.RUNNING) "Online" else "Offline"
            )
        })
        add(buildText {
            spacer("-")
            appendSpace()
            note("Spieler: ")
            variableValue("${server.getPlayerCount()} / ${server.maxPlayers}")
        })

        if (state == SurfServerState.RUNNING) {
            add(empty())
            add(buildText {
                spacer("» Klicke zum Verbinden")
            })
        }
    }

    /**
     * The lobby servers shown in the selector, sorted by their display name.
     */
    fun lobbyServers(): List<SurfServer> = SurfCoreApi
        .getServerByCategory(lobbyConfig.lobbyCategory)
        .sortedBy { it.displayName }
}
