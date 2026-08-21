package dev.slne.surf.lobby.core.client.jump

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*

class DoubleJumpTrackerTest {

    @Test
    fun `a jump on the ground followed by an airborne jump fires`() {
        val uuid = UUID.randomUUID()

        assertFalse(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = true))
        assertFalse(DoubleJumpTracker.onJumpInput(uuid, isJumping = false, isOnGround = false))
        assertTrue(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = false))
    }

    @Test
    fun `holding the jump key does not fire again`() {
        val uuid = UUID.randomUUID()

        DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = true)
        assertFalse(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = false))
    }

    @Test
    fun `an airborne jump without a first jump does not fire`() {
        val uuid = UUID.randomUUID()

        assertFalse(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = false))
    }

    @Test
    fun `a second airborne jump does not fire until the player grounds again`() {
        val uuid = UUID.randomUUID()

        DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = true)
        DoubleJumpTracker.onJumpInput(uuid, isJumping = false, isOnGround = false)
        assertTrue(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = false))

        DoubleJumpTracker.onJumpInput(uuid, isJumping = false, isOnGround = false)
        assertFalse(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = false))

        DoubleJumpTracker.onGrounded(uuid)
        DoubleJumpTracker.onJumpInput(uuid, isJumping = false, isOnGround = true)
        assertFalse(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = true))
        DoubleJumpTracker.onJumpInput(uuid, isJumping = false, isOnGround = false)
        assertTrue(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = false))
    }

    @Test
    fun `clearing forgets every jump state`() {
        val uuid = UUID.randomUUID()

        DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = true)
        DoubleJumpTracker.clear(uuid)

        assertFalse(DoubleJumpTracker.onJumpInput(uuid, isJumping = true, isOnGround = false))
    }
}
