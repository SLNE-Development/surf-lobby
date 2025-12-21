package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object ItemInteractListener : Listener {
    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return

        InventoryItem.items.forEach {
            if (item.isSimilar(it.item)) {
                it.onInteract(event.player)
                event.cancel()
            }
        }
    }
}