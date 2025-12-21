package dev.slne.surf.lobby.listener

import dev.slne.surf.surfapi.bukkit.api.event.cancel
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

object InventoryInteractListener : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        event.cancel()
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryClickEvent) {
        event.cancel()
    }

    @EventHandler
    fun onOffhandSwap(event: PlayerSwapHandItemsEvent) {
        event.cancel()
    }
}