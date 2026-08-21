package dev.slne.surf.lobby.listener

import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.paper.event.cancel
import dev.slne.surf.lobby.core.client.hook.SettingsHook
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.ElytraBoostManager
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

object InventoryInteractListener : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked.gameMode == GameMode.CREATIVE) {
            return
        }

        if (event.inventory.holder != event.whoClicked) {
            return
        }

        event.cancel()
    }

    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        event.cancel()
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryClickEvent) {
        if (event.whoClicked.gameMode == GameMode.CREATIVE) {
            return
        }

        if (event.inventory.holder != event.whoClicked) {
            return
        }

        event.cancel()
    }

    @EventHandler
    fun onOffhandSwap(event: PlayerSwapHandItemsEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        ElytraBoostManager.checkAndBoost(event.player)

        event.cancel()
    }

    @EventHandler
    fun onHoldItem(event: PlayerItemHeldEvent) {
        if (!SettingsHook.available) {
            return
        }

        if (!SettingsHook.hasScrollSoundsEnabled(event.player.uniqueId)) {
            return
        }

        if (InventoryItem.items.values.filter { item ->
                item.permission?.let { event.player.hasPermission(it) } ?: true
            }.any { it.slot == event.newSlot }) {
            event.player.playSound(true) {
                type(Sound.UI_BUTTON_CLICK)
            }
        }
    }
}