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
import net.kyori.adventure.text.format.TextDecoration

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
        darkSpacer("[Survival]")
    }

    val eventServerName = buildText { primary("Event Server") }

    val noLobbyAvailableName = buildText { error("Keine Lobby Server verfügbar") }

    fun survivalServerLore(): List<Component> = buildList {
        val survivalSurfServer = SurfCoreApi.getServerByName(lobbyConfig.survivalServerName)
        val survivalState = survivalSurfServer?.state

        add(empty())
        add(buildText {
            spacer("Erlebe das Survival Abenteuer, mit")
        })

        add(buildText {
            spacer("Freunden oder alleine.")
        })

        add(empty())
        add(buildText {
            spacer("Unter anderem:")
        })

        add(buildText {
            appendSpace()
            white("▪ ")
            spacer("Grundstücke: ")
            white("/protect", TextDecoration.UNDERLINED)
        })
        add(buildText {
            appendSpace()
            white("▪ ")
            spacer("Shopsystem: ")
            white("/shop", TextDecoration.UNDERLINED)
        })
        add(buildText {
            appendSpace()
            white("▪ ")
            spacer("Köpfe: ")
            white("/hdb", TextDecoration.UNDERLINED)
        })

        add(empty())

        if (survivalState == SurfServerState.RUNNING) {
            add(buildText {
                darkSpacer("» ")
                info("Spieler: ")
                append(coloredPlayerCount(survivalSurfServer.getPlayerCount(), " 👥"))
            })

            add(empty())
            add(buildText {
                white("꒑ ")
                note("Direkt Teleport auf den Survival Server ")
                darkSpacer("[")
                gold("Premium")
                darkSpacer("]")
            })
            add(buildText {
                white("ꊐ ")
                note("Teleport zum Survivalschiff")
            })
        } else {
            add(buildText {
                darkSpacer("» ")
                error("Derzeit nicht verfügbar")
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

    private fun coloredPlayerCount(playerCount: Int, extra: String) = buildText {
        when (playerCount) {
            0 -> error("${0}$extra")
            in 1..10 -> warning("$playerCount$extra")
            else -> success("$playerCount$extra")
        }
    }
}
