package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.utils.PermissionRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

object WorldProtectionListener : Listener {
    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        if (event.player.hasPermission(PermissionRegistry.PROTECTION_BYPASS)) {
            return
        }

        event.isCancelled = true
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (event.player.hasPermission(PermissionRegistry.PROTECTION_BYPASS)) {
            return
        }

        event.isCancelled = true
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.player.hasPermission(PermissionRegistry.PROTECTION_BYPASS)) {
            return
        }

        event.isCancelled = true
    }
}