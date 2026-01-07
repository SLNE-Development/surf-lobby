package dev.slne.surf.lobby.inventory.item

import dev.slne.surf.lobby.inventory.item.impl.navigator.NavigatorInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.pushback.PushbackDisableInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.pushback.PushbackEnableInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowAllPlayersInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowNonePlayersInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowTeamPlayersInventoryItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class InventoryItem(
    val slot: Int,
    val item: ItemStack
) {
    val permission: String? = null

    fun onInteract(player: Player) {}

    companion object {
        val items = mutableListOf<InventoryItem>()

        init {
            items.add(PushbackDisableInventoryItem)
            items.add(PushbackEnableInventoryItem)
            items.add(NavigatorInventoryItem)
            items.add(ShowNonePlayersInventoryItem)
            items.add(ShowTeamPlayersInventoryItem)
            items.add(ShowAllPlayersInventoryItem)
        }
    }
}