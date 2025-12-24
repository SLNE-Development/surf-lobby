package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.inventory.item.StatableInventoryItem
import dev.slne.surf.lobby.manager.PlayerVisibilityManager
import dev.slne.surf.lobby.manager.PushbackManager
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object PlayerConnectionListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.gameMode = GameMode.ADVENTURE

        InventoryItem.items.filter { item ->
            item.permission?.let { event.player.hasPermission(it) } ?: true
        }.forEach { item ->
            if (item is StatableInventoryItem<*>) {
                item.updatePlayerItem(event.player)
            } else {
                event.player.inventory.setItem(item.slot, item.item)
            }
        }
        
        // Update player visibility for all players
        PlayerVisibilityManager.onPlayerJoin(event.player)
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent) {
        PushbackManager.remove(event.player.uniqueId)
        PlayerVisibilityManager.remove(event.player.uniqueId)
    }
}