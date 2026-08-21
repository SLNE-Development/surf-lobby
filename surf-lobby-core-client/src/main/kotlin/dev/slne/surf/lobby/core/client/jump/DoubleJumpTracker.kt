package dev.slne.surf.lobby.core.client.jump

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterWrite
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.milliseconds

/**
 * Tracks the jump inputs of players and decides when a double jump fires.
 *
 * A double jump fires when a player presses the jump key twice within a short window while
 * being airborne on the second press. The tracker only handles the decision; applying the
 * boost is up to the caller.
 */
object DoubleJumpTracker {

    private val DOUBLE_JUMP_WINDOW = 350.milliseconds

    private val firstJumpWindow = Caffeine.newBuilder()
        .expireAfterWrite(DOUBLE_JUMP_WINDOW)
        .maximumSize(10_000)
        .build<UUID, Unit>()

    private val usedDoubleJump = ConcurrentHashMap.newKeySet<UUID>()
    private val jumpPressed = ConcurrentHashMap.newKeySet<UUID>()

    /**
     * Processes a jump input of the player identified by [uuid] and returns whether a double
     * jump fires now.
     */
    fun onJumpInput(uuid: UUID, isJumping: Boolean, isOnGround: Boolean): Boolean {
        if (!isJumping) {
            jumpPressed.remove(uuid)
            return false
        }

        val newJumpPress = jumpPressed.add(uuid)
        if (!newJumpPress) return false

        if (isOnGround) {
            usedDoubleJump.remove(uuid)
            firstJumpWindow.put(uuid, Unit)
            return false
        }

        val hasValidFirstJump = firstJumpWindow.getIfPresent(uuid) != null
        if (!hasValidFirstJump) return false

        val canDoubleJumpNow = usedDoubleJump.add(uuid)
        if (!canDoubleJumpNow) return false

        firstJumpWindow.invalidate(uuid)
        return true
    }

    /**
     * Resets the used double jump once the player identified by [uuid] touches the ground.
     */
    fun onGrounded(uuid: UUID) {
        usedDoubleJump.remove(uuid)
    }

    /**
     * Forgets every jump state of the player identified by [uuid].
     */
    fun clear(uuid: UUID) {
        firstJumpWindow.invalidate(uuid)
        usedDoubleJump.remove(uuid)
        jumpPressed.remove(uuid)
    }
}
