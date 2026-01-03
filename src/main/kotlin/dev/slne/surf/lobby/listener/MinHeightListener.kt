package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.lobbyConfig
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object MinHeightListener : Listener {
    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        val minHeight = lobbyConfig.minHeight

        if (!event.hasExplicitlyChangedPosition()) {
            return
        }

        if (player.location.y < minHeight) {
            player.teleportAsync(lobbyConfig.spawnPoint.toLocation()).thenRun {
                player.velocity.setY(0)
            }
        }
    }
}