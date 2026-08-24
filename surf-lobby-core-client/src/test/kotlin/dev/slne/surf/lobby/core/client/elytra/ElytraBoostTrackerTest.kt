package dev.slne.surf.lobby.core.client.elytra

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

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

    /**
     * Only one caller may start a boost, no matter how many threads race for it.
     */
    @Test
    fun `only one concurrent caller starts a boost`() {
        val threads = 8
        val uuid = UUID.randomUUID()
        val started = AtomicInteger()
        val start = CountDownLatch(1)
        val pool = Executors.newFixedThreadPool(threads)

        try {
            val tasks = List(threads) {
                pool.submit {
                    start.await()

                    if (ElytraBoostTracker.startBoosting(uuid)) {
                        started.incrementAndGet()
                    }
                }
            }

            start.countDown()
            tasks.forEach { it.get(30, TimeUnit.SECONDS) }
        } finally {
            pool.shutdownNow()
        }

        assertEquals(1, started.get())
        assertTrue(ElytraBoostTracker.isBoosting(uuid))
        assertTrue(ElytraBoostTracker.clear(uuid))
    }
}
