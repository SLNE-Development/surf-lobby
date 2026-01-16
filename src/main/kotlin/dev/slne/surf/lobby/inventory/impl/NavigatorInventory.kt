package dev.slne.surf.lobby.inventory.impl

import com.github.stefvanschie.inventoryframework.pane.util.Slot
import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.Locations
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.menu
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.staticPane
import dev.slne.surf.surfapi.bukkit.api.surfBukkitApi
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import org.bukkit.Sound
import org.bukkit.entity.Player

fun navigatorInventory() = menu(text("<shift:-46><glyph:server_selector>"), 6) {
    setOnGlobalDrag { it.cancel() }
    setOnGlobalClick { it.cancel() }

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

    staticPane(Slot.fromXY(0, 4), 2, 3) {
        fillWith(lobbyOneServerItem)

        setOnClick {
            val player = it.whoClicked as? Player ?: return@setOnClick
            surfBukkitApi.sendPlayerToServer(player, "lobby01")
        }
    }

    staticPane(Slot.fromXY(3, 4), 2, 3) {
        fillWith(lobbyTwoServerItem)

        setOnClick {
            val player = it.whoClicked as? Player ?: return@setOnClick
            surfBukkitApi.sendPlayerToServer(player, "lobby02")
        }
    }
    staticPane(Slot.fromXY(6, 4), 2, 3) {
        fillWith(lobbyThreeServerItem)

        setOnClick {
            val player = it.whoClicked as? Player ?: return@setOnClick
            surfBukkitApi.sendPlayerToServer(player, "lobby03")
        }
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

private val lobbyOneServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Lobby 1")
    }
}

private val lobbyTwoServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Lobby 2")
    }
}

private val lobbyThreeServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Lobby 3")
    }
}