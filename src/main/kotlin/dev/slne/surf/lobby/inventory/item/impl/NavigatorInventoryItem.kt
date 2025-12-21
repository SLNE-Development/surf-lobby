package dev.slne.surf.lobby.inventory.item.impl

import dev.slne.surf.lobby.inventory.impl.navigatorInventory
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object NavigatorInventoryItem : InventoryItem {
    override val slot = 4
    override val item = ItemType.COMPASS.createItemStack().apply {
        displayName {
            primary("Navigator")
        }

        buildLore {
            emptyLine()
            line {
                info("Klicke, um den Navigator zu öffnen")
            }
        }
    }
    override val permission: String? = null

    override fun onInteract(player: Player) {
        navigatorInventory().show(player)
    }
}