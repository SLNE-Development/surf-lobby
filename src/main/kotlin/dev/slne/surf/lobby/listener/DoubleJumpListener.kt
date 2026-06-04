package dev.slne.surf.lobby.listener

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterWrite
import dev.slne.surf.lobby.hook.parkour.ParkourHook
import dev.slne.surf.lobby.listener.DoubleJumpListener.doubleJumpWindow
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

/**
 * Handles the lobby double-jump mechanic.
 *
 * The first jump starts a short time window in which a second jump input
 * triggers a boosted jump. After a successful double jump, the player must
 * touch the ground again before another double jump can be performed.
 */
object DoubleJumpListener : Listener {

    /**
     * The maximum time between the first and second jump input.
     */
    private val doubleJumpWindow = 350.milliseconds

    /**
     * Stores players that recently pressed jump once.
     *
     * Entries expire automatically after [doubleJumpWindow], so a second jump
     * only counts if it happens shortly after the first one.
     */
    private val lastJump = Caffeine.newBuilder()
        .expireAfterWrite(doubleJumpWindow)
        .build<UUID, Boolean>()

    /**
     * Stores players that have already used their double jump and must touch
     * the ground before they can double jump again.
     */
    private val needsGroundReset = ConcurrentHashMap.newKeySet<UUID>()

    private val holdingJump = ConcurrentHashMap.newKeySet<UUID>()

    /**
     * Handles jump input and triggers the double jump when the player presses
     * jump twice within the configured time window.
     *
     * Players that are currently inside a parkour session or still need to
     * touch the ground again are ignored.
     *
     * @param event the player input event
     */
    @EventHandler(ignoreCancelled = true)
    fun onInput(event: PlayerInputEvent) {
        val player = event.player
        val uuid = player.uniqueId

        val isJumping = event.input.isJump
        if (isJumping) {
            if (!holdingJump.add(uuid)) return
        } else {
            holdingJump.remove(uuid)
            return
        }

        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return
        if (needsGroundReset.contains(uuid)) return
        if (plugin.checkParkourHook() && ParkourHook.isInParkour(player)) return

        var doDoubleJump = false
        lastJump.asMap().compute(uuid) { _, previous ->
            if (previous == null) {
                true // first jump — mark jump window
            } else {
                doDoubleJump = true
                null // second jump — consume jump window
            }
        }

        if (!doDoubleJump) return

        needsGroundReset.add(uuid)

        player.velocity = player.eyeLocation.direction.multiply(2).setY(1.0)
        player.world.spawnParticle(Particle.EXPLOSION, player.location, 10)
    }

    /**
     * Clears the double-jump reset state once a player touches the ground again.
     *
     * This allows the player to perform another double jump after landing.
     *
     * @param event the player movement event
     */
    @EventHandler(ignoreCancelled = true)
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!event.hasExplicitlyChangedBlock()) return

        val player = event.player
        val uuid = player.uniqueId

        if (!needsGroundReset.contains(uuid)) return
        if (!player.isTouchingGround()) return

        needsGroundReset.remove(uuid)
        lastJump.invalidate(uuid)
    }

    /**
     * Cleans up the double-jump state when a player leaves the server.
     *
     * @param event the player quit event
     */
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        needsGroundReset.remove(event.player.uniqueId)
    }

    /**
     * Checks whether the player is standing on a non-empty block.
     *
     * @return `true` if there is a non-empty block below the player
     */
    private fun Player.isTouchingGround(): Boolean {
        val blockLocation = location.toBlockLocation()
        blockLocation.y -= 1
        return blockLocation.block.isSolid
    }
}