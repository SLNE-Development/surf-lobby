package dev.slne.surf.lobby.listener

import dev.slne.surf.api.core.util.mutableObject2ObjectMapOf
import dev.slne.surf.lobby.hook.parkour.ParkourHook
import dev.slne.surf.lobby.plugin
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInputEvent
import java.util.*

object DoubleJumpListener : Listener {
    private val lastJump = mutableObject2ObjectMapOf<UUID, Long>()
    private val lastGround = mutableObject2ObjectMapOf<UUID, Long>()
    private val lastJumpState = mutableObject2ObjectMapOf<UUID, Boolean>()

    private const val DOUBLE_JUMP_WINDOW = 350L
    private const val GROUND_GRACE = 150L

    @EventHandler
    fun onInput(event: PlayerInputEvent) {
        val player = event.player
        val uuid = player.uniqueId

        val isJumping = event.input.isJump
        val wasJumping = lastJumpState[uuid] ?: false
        lastJumpState[uuid] = isJumping

        if (!isJumping || wasJumping) {
            return
        }

        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) {
            return
        }

        if (plugin.checkParkourHook() && ParkourHook.isInParkour(player)) {
            return
        }

        val now = System.currentTimeMillis()

        if (player.isOnGround) {
            lastGround[uuid] = now
        }

        val lastGroundTime = lastGround[uuid] ?: 0L
        val lastJumpTime = lastJump[uuid]

        val recentlyOnGround = now - lastGroundTime <= GROUND_GRACE

        if (lastJumpTime != null &&
            now - lastJumpTime <= DOUBLE_JUMP_WINDOW &&
            recentlyOnGround
        ) {
            player.velocity = player.eyeLocation.direction.multiply(2).setY(1.0)
            player.world.spawnParticle(Particle.EXPLOSION, player.location, 10)

            lastJump.remove(uuid)
            lastJumpState[uuid] = false
            return
        }

        if (recentlyOnGround) {
            lastJump[uuid] = now
        }
    }
}