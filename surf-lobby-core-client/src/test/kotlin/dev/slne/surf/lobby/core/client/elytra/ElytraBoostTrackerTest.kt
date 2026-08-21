package dev.slne.surf.lobby.core.client.elytra

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class ElytraBoostTrackerTest {

    @Test
    fun `starting a boost marks the player as boosting once`() {
        val uuid = UUID.randomUUID()

        assertFalse(ElytraBoostTracker.isBoosting(uuid))
        assertTrue(ElytraBoostTracker.startBoosting(uuid))
        assertTrue(ElytraBoostTracker.isBoosting(uuid))
        assertFalse(ElytraBoostTracker.startBoosting(uuid))

        ElytraBoostTracker.clear(uuid)
    }

    @Test
    fun `marking a boost starts the cooldown`() {
        val uuid = UUID.randomUUID()

        assertFalse(ElytraBoostTracker.isOnCooldown(uuid))

        ElytraBoostTracker.markBoosted(uuid)

        assertTrue(ElytraBoostTracker.isOnCooldown(uuid))
    }

    @Test
    fun `clearing reports whether the player was boosting`() {
        val uuid = UUID.randomUUID()

        ElytraBoostTracker.startBoosting(uuid)

        assertTrue(ElytraBoostTracker.clear(uuid))
        assertFalse(ElytraBoostTracker.isBoosting(uuid))
        assertFalse(ElytraBoostTracker.clear(uuid))
    }

    @Test
    fun `clearing a boost does not clear its cooldown`() {
        val uuid = UUID.randomUUID()

        ElytraBoostTracker.startBoosting(uuid)
        ElytraBoostTracker.markBoosted(uuid)

        assertTrue(ElytraBoostTracker.clear(uuid))
        assertFalse(ElytraBoostTracker.isBoosting(uuid))
        assertTrue(ElytraBoostTracker.isOnCooldown(uuid))
    }
}