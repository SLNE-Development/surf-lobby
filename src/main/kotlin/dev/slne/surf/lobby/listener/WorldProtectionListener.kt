package dev.slne.surf.lobby.listener

import dev.slne.surf.surfapi.bukkit.api.event.cancel
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

object WorldProtectionListener : Listener {
    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        event.cancel()
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        event.cancel()
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        event.cancel()
    }

    @EventHandler
    fun onInteractAtEntity(event: PlayerInteractAtEntityEvent) {
        if (event.player.gameMode == GameMode.CREATIVE) {
            return
        }

        event.cancel()
    }
}