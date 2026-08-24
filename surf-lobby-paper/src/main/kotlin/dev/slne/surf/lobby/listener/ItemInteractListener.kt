package dev.slne.surf.lobby.listener

import dev.slne.surf.api.paper.event.cancel
import dev.slne.surf.lobby.inventory.item.InventoryItem
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

        val player = event.player
        val type = item.type

        for (candidate in InventoryItem.all) {
            if (type != candidate.item.type) {
                continue
            }

            if (item.isSimilar(candidate.item) ||
                item.isSimilar(candidate.getItemForPlayer(player))
            ) {
                candidate.onInteract(player)
                event.cancel()
            }
        }
    }
}