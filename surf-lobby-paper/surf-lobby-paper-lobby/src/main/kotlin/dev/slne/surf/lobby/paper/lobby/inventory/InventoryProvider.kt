package dev.slne.surf.lobby.paper.lobby.inventory

import dev.slne.surf.lobby.paper.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.paper.lobby.inventory.item.InventoryItemAction
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component

@Component
class InventoryProvider(private val items: ObjectProvider<InventoryItem>) {

    fun provide(player: Player) {
        clear(player)

        items.forEach {
            player.inventory.setItem(it.meta.slot, it.buildItemStack(player))
        }
    }

    fun clear(player: Player) {
        player.inventory.clear()
    }

    fun handleBlockAction(event: PlayerInteractEvent) {
        val itemStack = event.item ?: return
        val action = event.action

        items.forEach { item ->
            if (item.isInventoryItem(itemStack)) {
                item.onClick(
                    event.player,
                    InventoryItemAction(
                        blockAction = action
                    )
                )
            }
        }
    }

    fun handleInventoryAction(event: InventoryClickEvent) {
        val itemStack = event.currentItem ?: return
        val action = event.action

        items.forEach { item ->
            if (item.isInventoryItem(itemStack)) {
                item.onClick(
                    event.whoClicked as Player,
                    InventoryItemAction(
                        inventoryAction = action
                    )
                )
            }
        }
    }

}
