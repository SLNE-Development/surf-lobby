package dev.slne.surf.lobby.paper.lobby.inventory

import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.springframework.stereotype.Component

@Component
class InventoryProviderListener(
    private val inventoryProvider: InventoryProvider
) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onInventory(event: InventoryEvent) { // TODO: Check if this works?
        if (event is Cancellable) {
            event.isCancelled = true
        }
    }
    
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        inventoryProvider.handleBlockAction(event)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        inventoryProvider.handleInventoryAction(event)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        inventoryProvider.provide(event.player)
    }

}