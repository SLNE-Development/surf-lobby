package dev.slne.surf.lobby.core.client.elytra

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterWrite
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

/**
 * Tracks which players are currently boosting with the elytra and whether they are on cooldown.
 */
object ElytraBoostTracker {
    private val boostingPlayers: MutableSet<UUID> = ConcurrentHashMap.newKeySet()

    private val boostCooldowns = Caffeine.newBuilder()
        .expireAfterWrite(2.seconds)
        .build<UUID, Unit>()

    fun isBoosting(uuid: UUID): Boolean = boostingPlayers.contains(uuid)

    /**
     * Marks the player identified by [uuid] as boosting.
     *
     * @return `true` if this call started the boost, or `false` if the player was already boosting
     */
    fun startBoosting(uuid: UUID): Boolean = boostingPlayers.add(uuid)

    /**
     * Returns whether the player identified by [uuid] is currently on boost cooldown.
     */
    fun isOnCooldown(uuid: UUID): Boolean = boostCooldowns.getIfPresent(uuid) != null

    /**
     * Starts the boost cooldown for the player identified by [uuid].
     */
    fun markBoosted(uuid: UUID) {
        boostCooldowns.put(uuid, Unit)
    }

    /**
     * Stops tracking the boost of the player identified by [uuid].
     *
     * @return whether the player was boosting
     */
    fun clear(uuid: UUID): Boolean = boostingPlayers.remove(uuid)
}
