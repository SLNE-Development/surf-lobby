package dev.slne.surf.lobby.inventory.impl

import com.github.stefvanschie.inventoryframework.pane.util.Slot
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.event.state.LocalEventServerState
import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.menu
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.staticPane
import dev.slne.surf.surfapi.bukkit.api.surfBukkitApi
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
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

            when (eventServerBridge.state) {
                LocalEventServerState.OPEN -> {
                    surfBukkitApi.sendPlayerToServer(player, "event")
                    return@setOnClick
                }

                LocalEventServerState.CLOSED -> {
                    if (player.hasPermission(PermissionRegistry.EVENT_BYPASS)) {
                        surfBukkitApi.sendPlayerToServer(player, "event")
                        return@setOnClick
                    }
                    player.sendText {
                        appendPrefix()
                        error("Der Event Server ist aktuell geschlossen!")
                    }
                }

                LocalEventServerState.UNKNOWN -> {
                    player.sendText {
                        appendPrefix()
                        error("Aktuell findet kein Event statt!")
                    }
                }
            }
            it.whoClicked.closeInventory()
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

            player.teleportAsync(lobbyConfig.survivalNpcTeleport.toLocation()).thenRun {
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

private val eventServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Event Server")
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