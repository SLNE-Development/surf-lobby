package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.config.toLocation
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.manager.ElytraBoostManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object PlayerMoveListener : Listener {
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
            return
        }

        @Suppress("DEPRECATION") // isOnGround is deprecated, but it works fine for our use case as it is only used to check if the player is on the ground to clear the boost, and it is not used for any critical logic.
        if (player.isOnGround) {
            ElytraBoostManager.clearBoost(player)
        }
    }
}