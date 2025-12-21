package dev.slne.surf.lobby.inventory.item

import dev.slne.surf.lobby.inventory.item.impl.PushbackDisableInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.PushbackEnableInventoryItem
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
            items.add(PushbackDisableInventoryItem)
            items.add(PushbackEnableInventoryItem)
        }
    }
}