package dev.slne.surf.lobby.paper.lobby.features.navigator.lobby

import com.github.shynixn.mccoroutine.folia.launch
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane
import dev.slne.surf.cloud.api.common.player.CloudPlayerManager
import dev.slne.surf.cloud.api.common.player.toCloudPlayer
import dev.slne.surf.cloud.api.common.server.CloudServer
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.addItem
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.childMenu
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.slot
import dev.slne.surf.surfapi.bukkit.api.inventory.types.SurfChestGui
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component.text
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

fun SurfChestGui.lobbySwitcherInventory() = childMenu(text("Lobby wechseln"), 6) {
    val lobbies = CloudServer.all().filter { it.lobby }

    val pane = PaginatedPane(slot(0), 9, 5).apply {
        populateWithGuiItems(lobbies.map { lobby ->
            val stack = ItemType.PAPER.createItemStack().apply {
                displayName {
                    primary(lobby.name)
                }
                buildLore {
                    emptyLine()
                    line {
                        info("Online: ${lobby.currentPlayerCount}/${lobby.maxPlayerCount}")
                    }
                }
            }

            GuiItem(stack) { click ->
                val player = click.whoClicked as? Player ?: return@GuiItem
                player.closeInventory()
                if (!lobby.hasEmptySlots()) {
                    player.sendText {
                        error("Die Lobby ${lobby.name} ist voll!")
                    }
                    return@GuiItem
                }

                player.toCloudPlayer()?.let { cloudPlayer ->
                    dev.slne.surf.lobby.paper.common.plugin.launch {
                        val result = cloudPlayer.connectToServerOrQueue(lobby, )
                        if (!result.isSuccess) {
                            player.sendText {
                                appendPrefix()
                                append(result.message)
                            }
                        }
                    }
                }
            }
        })
    }

    val previousGuiItem = GuiItem(ItemType.ARROW.createItemStack().apply {
        displayName {
            primary("Zurück")
        }
    }) { click ->
        if (pane.page == 0) {
            click.whoClicked.sendText {
                error("Du bist bereits auf der ersten Seite")
            }
        } else {
            pane.page -= 1
            update()
        }
    }

    val nextGuiItem = GuiItem(ItemType.ARROW.createItemStack().apply {
        displayName {
            primary("Weiter")
        }
    }) { click ->
        if (pane.page + 1 >= pane.pages) {
            click.whoClicked.sendText {
                error("Du bist bereits auf der letzten Seite")
            }
        } else {
            pane.page += 1
            update()
        }
    }

    addItem(slot(8, 4), previousGuiItem)
    addItem(slot(8, 5), nextGuiItem)
    addPane(pane)
}