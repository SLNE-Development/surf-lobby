package dev.slne.surf.lobby.paper.lobby.features

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.springframework.stereotype.Component

@Component
class DoubleJumpListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.getPlayer()

        if (player.gameMode != GameMode.CREATIVE && player.location.block.getRelative(BlockFace.DOWN).type != Material.AIR && !player.isFlying) {
            player.allowFlight = true
        }
    }

    @EventHandler
    fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
        val player = event.getPlayer()

        if (player.gameMode == GameMode.CREATIVE) {
            return
        }

        val direction = player.eyeLocation.direction
        val loc = player.location

        player.velocity = direction.multiply(2).setY(1)
        loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 10)

        player.allowFlight = false
        player.isFlying = false

        event.isCancelled = true
    }
}