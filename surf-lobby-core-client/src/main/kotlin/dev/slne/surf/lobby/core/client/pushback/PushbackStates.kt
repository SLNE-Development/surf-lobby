package dev.slne.surf.lobby.core.client.pushback

import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PushbackStates {
    private val pushbacks: MutableSet<UUID> = ConcurrentHashMap.newKeySet()

    /**
     * The radius in blocks within which other players are pushed away.
     */
    const val RANGE = 3.0

    /**
     * The horizontal pushback strength in blocks per tick.
     */
    const val FORCE = -0.5

    /**
     * The vertical pushback strength in blocks per tick.
     */
    const val Y_FORCE = 0.5

    fun add(uuid: UUID) {
        pushbacks.add(uuid)
    }

    fun remove(uuid: UUID) {
        pushbacks.remove(uuid)
    }

    fun all(): Set<UUID> = pushbacks
}
