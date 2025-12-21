@file:Suppress("UnstableApiUsage")

package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.inventory.item.InventoryItem
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
        event.player.inventory.clear()

        InventoryItem.items.filter { item ->
            item.permission?.let { event.player.hasPermission(it) } ?: true
        }.forEach {
            event.player.inventory.setItem(it.slot, it.item)
        }
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent) {
        PushbackManager.remove(event.player.uniqueId)
    }
}