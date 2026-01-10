package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.parkour.ParkourHook
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent

object DoubleJumpListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {

        if (!event.hasExplicitlyChangedBlock()) {
            return
        }

        val player = event.getPlayer()

        if (ParkourHook.isInParkour(player)) {
            return
        }


        if (player.gameMode != GameMode.CREATIVE &&
            player.isOnGround &&
            !player.isFlying
        ) {
            player.allowFlight = true
        }
    }

    @EventHandler
    fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
        val player = event.getPlayer()

        if (player.gameMode == GameMode.CREATIVE) {
            return
        }

        if (ParkourHook.isInParkour(player)) {
            return
        }

        if (!player.allowFlight) {
            return
        }

        val direction = player.eyeLocation.direction
        val loc = player.location

        player.velocity = direction.multiply(2).setY(1)
        loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 10)

        player.allowFlight = false
        player.isFlying = false

        event.cancel()
    }
}