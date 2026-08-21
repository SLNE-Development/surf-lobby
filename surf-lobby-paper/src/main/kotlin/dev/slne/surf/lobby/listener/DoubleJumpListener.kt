package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.core.client.hook.ParkourHook
import dev.slne.surf.lobby.core.client.jump.DoubleJumpTracker
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInputEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent


object DoubleJumpListener : Listener {

    @EventHandler(ignoreCancelled = true)
    fun onPlayerInput(event: PlayerInputEvent) {
        val player = event.player
        val uuid = player.uniqueId

        if (!canUseDoubleJump(player)) {
            DoubleJumpTracker.clear(uuid)
            return
        }

        @Suppress("DEPRECATION")
        val fires = DoubleJumpTracker.onJumpInput(uuid, event.input.isJump, player.isOnGround)
        if (!fires) return

        player.velocity = player.eyeLocation.direction.multiply(2).setY(1.0)
        player.world.spawnParticle(Particle.EXPLOSION, player.location, 10)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.hasExplicitlyChangedPosition()) return
        val player = event.player

        @Suppress("DEPRECATION")
        if (player.isOnGround) {
            DoubleJumpTracker.onGrounded(player.uniqueId)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        DoubleJumpTracker.clear(event.player.uniqueId)
    }

    private fun canUseDoubleJump(player: Player): Boolean {
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return false
        if (ParkourHook.available && ParkourHook.isInParkour(player.uniqueId)) return false
        return true
    }
}
