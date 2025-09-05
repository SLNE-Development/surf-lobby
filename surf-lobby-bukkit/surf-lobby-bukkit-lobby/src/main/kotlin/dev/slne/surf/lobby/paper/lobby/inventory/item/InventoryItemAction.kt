package dev.slne.surf.lobby.paper.lobby.inventory.item

import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryAction

data class InventoryItemAction(
    val blockAction: Action? = null,
    val inventoryAction: InventoryAction? = null
) {
    val isLeftClick: Boolean
        get() {
            if (blockAction != null) {
                return blockAction == Action.LEFT_CLICK_AIR || blockAction == Action.LEFT_CLICK_BLOCK
            }

            if (inventoryAction != null) {
                return inventoryAction == InventoryAction.PICKUP_ALL
            }

            return false
        }

    val isRightClick: Boolean
        get() {
            if (blockAction != null) {
                return blockAction == Action.RIGHT_CLICK_AIR || blockAction == Action.RIGHT_CLICK_BLOCK
            }

            if (inventoryAction != null) {
                return inventoryAction == InventoryAction.PICKUP_HALF ||
                        inventoryAction == InventoryAction.PICKUP_ONE
            }

            return false
        }
}