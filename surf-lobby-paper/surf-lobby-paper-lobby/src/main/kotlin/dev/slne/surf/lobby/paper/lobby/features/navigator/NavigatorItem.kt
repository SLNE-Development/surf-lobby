package dev.slne.surf.lobby.paper.lobby.features.navigator

import dev.slne.surf.lobby.paper.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.paper.lobby.inventory.item.InventoryItemAction
import dev.slne.surf.lobby.paper.lobby.inventory.item.items.InventoryItemMeta
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

@InventoryItemMeta(slot = 4)
class NavigatorItem : InventoryItem() {
    override fun supplyItemStack(player: Player) = ItemType.COMPASS.createItemStack().apply {
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

    override fun onClick(
        player: Player,
        action: InventoryItemAction
    ) {
        navigatorInventory().show(player)
    }
}