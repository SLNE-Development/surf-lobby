package dev.slne.surf.lobby.core.client.menu

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.key
import dev.slne.surf.api.core.minimessage.miniMessage
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.server.SurfServer
import dev.slne.surf.core.api.common.server.state.SurfServerState
import dev.slne.surf.event.state.EventServerState
import dev.slne.surf.event.data.EventDataSource
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.event.EventServerBridge
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.empty
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

object NavigatorContents {
    val lobbySelectorName = buildText { primary("Lobby Auswahl") }
    val spawnName = buildText { primary("Spawn") }
    val rulesName = buildText { primary("Regelwerk") }
    val cosmeticsName = buildText { primary("???") }

    val survivalServerName = buildText {
        append(miniMessage.deserialize("<gradient:#6edb53:#2cf58d>CastSMP "))
        darkSpacer("[Survival]")
    }

    val eventServerName = buildText {
        val currentEvent = EventDataSource.getActiveEvents().maxByOrNull { it.startDate }
        if (currentEvent == null) {
            append(miniMessage.deserialize("<gradient:#34e1eb:#1fb2db>Event Server "))
            darkSpacer("[Event]")
        } else {
            append(miniMessage.deserialize("<gradient:#34e1eb:#1fb2db>${currentEvent.displayName} "))
            darkSpacer("[Event]")
        }

    }
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
                append(coloredPlayerCount(survivalSurfServer.getPlayerCount(), " 👤"))
            })

            add(empty())
            add(buildText {
                append {
                    white("ꑰ")
                    font(key("surf:menu"))
                }
                appendSpace()
                white("Verbinden zum Survival Server ".toSmallCaps())
                darkSpacer("[")
                note("Premium")
                darkSpacer("]")
            })
            add(buildText {
                append {
                    white("ꑲ")
                    font(key("surf:menu"))
                }
                appendSpace()

                white("Teleport zum Survival-Schiff".toSmallCaps())
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
        val eventState = EventServerBridge.state.get()

        val currentEvent = EventDataSource.getActiveEvents().maxByOrNull { it.startDate }

        add(empty())

        if (currentEvent == null) {
            add(buildText {
                spacer("Derzeit findet kein Event statt.")
            })
        } else {
            currentEvent.description.split("<br>").forEach {
                add(buildText {
                    spacer(it)
                })
            }

            if (currentEvent.features.isNotEmpty()) {
                add(empty())
                add(buildText {
                    spacer("Unter anderem:")
                })

                currentEvent.features.shuffled().take(3).forEach { feature ->
                    add(buildText {
                        appendSpace()
                        white("▪ ")
                        spacer("${feature.displayName}: ")
                        white(feature.additions, TextDecoration.UNDERLINED)
                    })
                }
            }

            add(empty())

            if (eventSurfServer?.state == SurfServerState.RUNNING) {
                add(buildText {
                    darkSpacer("» ")
                    info("Spieler: ")
                    append(coloredPlayerCount(eventSurfServer.getPlayerCount(), " 👤"))
                })

                if (eventState == EventServerState.OPEN) {
                    add(empty())
                    add(buildText {
                        append {
                            white("ꑰ")
                            font(key("surf:menu"))
                        }
                        appendSpace()

                        white("Verbinden zum Event Server ".toSmallCaps())
                        darkSpacer("[")
                        note("Premium")
                        darkSpacer("]")
                    })
                    add(buildText {
                        append {
                            white("ꑲ")
                            font(key("surf:menu"))
                        }
                        appendSpace()
                        white("Teleport zum Event-Schiff".toSmallCaps())
                    })
                } else {
                    add(buildText {
                        darkSpacer("» ")
                        error("Derzeit geschlossen")
                    })
                }
            } else {
                add(buildText {
                    darkSpacer("» ")
                    error("Derzeit nicht verfügbar")
                })
            }
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

    private val COLOR_GOLD = TextColor.color(0xFCC500)
}
