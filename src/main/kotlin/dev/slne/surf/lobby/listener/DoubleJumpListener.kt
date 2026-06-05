package dev.slne.surf.lobby.listener

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterWrite
import dev.slne.surf.lobby.hook.parkour.ParkourHook
import dev.slne.surf.lobby.plugin
import org.bukkit.GameMode
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInputEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.milliseconds


object DoubleJumpListener : Listener {

    private val DOUBLE_JUMP_WINDOW = 350.milliseconds

    private val firstJumpWindow = Caffeine.newBuilder()
        .expireAfterWrite(DOUBLE_JUMP_WINDOW)
        .maximumSize(10_000)
        .build<UUID, Unit>()

    private val usedDoubleJump = ConcurrentHashMap.newKeySet<UUID>()
    private val jumpPressed = ConcurrentHashMap.newKeySet<UUID>()

    @EventHandler(ignoreCancelled = true)
    fun onPlayerInput(event: PlayerInputEvent) {
        val player = event.player
        val uuid = player.uniqueId
        val isJumping = event.input.isJump

        if (!canUseDoubleJump(player)) {
            clearPlayer(uuid)
            return
        }

        if (!isJumping) {
            jumpPressed.remove(uuid)
            return
        }

        val newJumpPress = jumpPressed.add(uuid)
        if (!newJumpPress) return

        @Suppress("DEPRECATION")
        if (player.isOnGround) {
            usedDoubleJump.remove(uuid)
            firstJumpWindow.put(uuid, Unit)
            return
        }

        val hasValidFirstJump = firstJumpWindow.getIfPresent(uuid) != null
        if (!hasValidFirstJump) return

        val canDoubleJumpNow = usedDoubleJump.add(uuid)
        if (!canDoubleJumpNow) return

        firstJumpWindow.invalidate(uuid)

        player.velocity = player.eyeLocation.direction.multiply(2).setY(1.0)
        player.world.spawnParticle(Particle.EXPLOSION, player.location, 10)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.hasExplicitlyChangedPosition()) return
        val player = event.player

        @Suppress("DEPRECATION")
        if (player.isOnGround) {
            usedDoubleJump.remove(player.uniqueId)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        clearPlayer(event.player.uniqueId)
    }

    private fun clearPlayer(uuid: UUID) {
        firstJumpWindow.invalidate(uuid)
        usedDoubleJump.remove(uuid)
        jumpPressed.remove(uuid)
    }

    private fun canUseDoubleJump(player: Player): Boolean {
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return false
        if (plugin.checkParkourHook() && ParkourHook.isInParkour(player)) return false
        return true
    }
}