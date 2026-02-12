package dev.slne.surf.lobby.inventory.impl

import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import com.github.stefvanschie.inventoryframework.pane.util.Slot
import dev.slne.surf.core.api.common.server.state.SurfServerState
import dev.slne.surf.core.api.common.surfCoreApi
import dev.slne.surf.core.api.paper.util.surfPlayer
import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.Locations
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.menu
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.staticPane
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.*
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

fun navigatorInventory() = menu(text("<shift:-48><glyph:server_selector>"), 6) {
    setOnGlobalDrag { it.cancel() }
    setOnGlobalClick { it.cancel() }

    staticPane(Slot.fromXY(1, 0), 3, 3) {
        fillWith(survivalServerItem)

        setOnClick {
            val player = it.whoClicked as? Player ?: return@setOnClick

//            if(player.isPremium()) { TODO: Premium Rang
//                surfBukkitApi.sendPlayerToServer(player, "survival")
//                return@setOnClick
//            }

            player.teleportAsync(Locations.SURVIVAL_TELEPORT.getLocation()).thenRun {
                player.playSound(true) {
                    type(Sound.ENTITY_ENDERMAN_TELEPORT)
                    pitch(2.0f)
                }
            }
            player.closeInventory()
        }
    }

    staticPane(Slot.fromXY(5, 0), 3, 3) {
        fillWith(eventServerItem)

        setOnClick {
            val player = it.whoClicked as? Player ?: return@setOnClick

//            if(player.isPremium()) { TODO: Premium Rang
//                surfBukkitApi.sendPlayerToServer(player, "event")
//                return@setOnClick
//            }

            player.teleportAsync(Locations.EVENT_TELEPORT.getLocation()).thenRun {
                player.playSound(true) {
                    type(Sound.ENTITY_ENDERMAN_TELEPORT)
                    pitch(2.0f)
                }
            }
            player.closeInventory()
        }
    }

    staticPane(Slot.fromXY(0, 4), 2, 2) {
        fillWith(lobbySelectorItem)

        setOnClick {
            lobbySelectorInventory().show(it.whoClicked)
        }
    }

    staticPane(Slot.fromXY(2, 4), 2, 2) {
        fillWith(spawnItem)

        setOnClick {
            it.whoClicked.closeInventory()
            it.whoClicked.teleportAsync(lobbyConfig.spawnPoint.toLocation()).thenRun {
                it.whoClicked.sendText {
                    appendInfoPrefix()
                    info("Du wurdest zum Spawn teleportiert.")
                }
            }
        }
    }

    staticPane(Slot.fromXY(5, 4), 2, 2) {
        fillWith(rulesItem)

        setOnClick {
            it.whoClicked.closeInventory()
            it.whoClicked.sendText {
                appendInfoPrefix()
                info("Das Regelwerk findest du hier: ")
                append {
                    variableValue("server.castcrafter.de/rules")
                    clickOpensUrl("https://server.castcrafter.de/rules")
                }
            }
        }
    }

    staticPane(Slot.fromXY(7, 4), 2, 2) {
        fillWith(cosmeticsItem)
    }
}

@Suppress("UnstableApiUsage")
fun lobbySelectorInventory() = menu(buildText { spacer("Lobby Auswahl") }, 3) {
    setOnGlobalDrag { it.cancel() }
    addPane(StaticPane(0, 0, 9, 3).apply {
        fillWith(ItemType.GRAY_STAINED_GLASS_PANE.createItemStack())
    })
    addPane(PaginatedPane(1, 1, 7, 1).apply {
        populateWithGuiItems(surfCoreApi.getServerByCategory("lobby").map {
            GuiItem(buildItem(ItemType.RECOVERY_COMPASS) {
                displayName { variableValue(it.name) }

                buildLore {
                    emptyLine()
                    line {
                        spacer("-")
                        appendSpace()
                        note("Status: ")
                        variableValue(
                            if (it.state == SurfServerState.RUNNING) {
                                "Online"
                            } else {
                                "Offline"
                            }
                        )
                    }
                    line {
                        spacer("-")
                        appendSpace()
                        note("Spieler: ")
                        variableValue("${it.getPlayerCount()} / ${it.maxPlayers}")
                    }
                    if (it.state == SurfServerState.RUNNING) {
                        emptyLine()
                        line {
                            spacer("» Klicke, um diesem Lobby Server beizutreten")
                        }
                    }
                }
            }) { event ->
                val player = event.whoClicked as? Player ?: return@GuiItem
                val updatedServer = surfCoreApi.getServerByName(it.name) ?: return@GuiItem

                if (updatedServer.state != SurfServerState.RUNNING) {
                    player.sendText {
                        appendInfoPrefix()
                        error("Dieser Server ist derzeit nicht erreichbar!")
                    }
                    return@GuiItem
                }

                updatedServer.pullPlayers(player.surfPlayer)
            }
        })
    })
    setOnGlobalClick { it.cancel() }
}

private val lobbySelectorItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Lobby Auswahl")
    }
}

private val spawnItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Spawn")
    }
}

private val rulesItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Regelwerk")
    }
}

private val cosmeticsItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("???")
    }
}

private val survivalServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Survival Server")
    }
}

private val eventServerItem
    get() = plugin.getInvisibleItem().apply {
        displayName {
            primary("Event Server")
        }

        buildLore {
            emptyLine()
            line {
                note("Status:".toSmallCaps())
            }
            line {
                when (eventServerBridge.state.get()) {
                    EventServerState.OPEN -> {
                        success("Klicke, um dem Event Server beizutreten")
                    }

                    EventServerState.CLOSED -> {
                        error("Der Event Server ist aktuell geschlossen")
                    }

                    EventServerState.UNKNOWN -> {
                        error("Aktuell findet kein Event statt")
                    }
                }
            }
            if (eventServerBridge.state.get() != EventServerState.UNKNOWN) {
                emptyLine()
                line {
                    note("Spieler:".toSmallCaps())
                }

                line {
                    val playerCount = eventServerBridge.currentEventPlayers.get()
                    val maxPlayers = eventServerBridge.currentEventMaxPlayers.get()
                    info("$playerCount / $maxPlayers Spieler online")
                }
            }

        }
    }