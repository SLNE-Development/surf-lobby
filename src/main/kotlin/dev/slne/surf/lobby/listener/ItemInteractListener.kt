package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object ItemInteractListener : Listener {
    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val item = event.item ?: return

        if (event.hand != EquipmentSlot.HAND) {
            return
        }

        InventoryItem.items.forEach {
            if (item.isSimilar(it.item) || item.isSimilar(it.getItemForPlayer(event.player))) {
                it.onInteract(event.player)
                event.cancel()
            }
        }
    }
}