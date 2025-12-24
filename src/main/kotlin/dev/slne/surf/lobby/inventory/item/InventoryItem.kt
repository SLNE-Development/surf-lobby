package dev.slne.surf.lobby.inventory.item

import dev.slne.surf.lobby.inventory.item.impl.NavigatorInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.PlayerVisibilityInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.PushbackInventoryItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface InventoryItem {
    val slot: Int
    val item: ItemStack
    val permission: String?

    fun onInteract(player: Player)

    companion object {
        val items = mutableListOf<InventoryItem>()

        init {
            items.add(PushbackInventoryItem)
            items.add(NavigatorInventoryItem)
            items.add(PlayerVisibilityInventoryItem)
        }
    }
}