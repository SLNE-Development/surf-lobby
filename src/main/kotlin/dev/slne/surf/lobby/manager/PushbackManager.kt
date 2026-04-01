package dev.slne.surf.lobby.manager

import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import org.bukkit.util.BoundingBox
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PushbackManager {
    private val pushbacks = mutableObjectSetOf<UUID>()
    const val RANGE = 3.0
    private const val FORCE = -0.5
    private const val Y_FORCE = 0.5

    val hitboxes = ConcurrentHashMap<UUID, BoundingBox>()

    fun hasPushback(uuid: UUID) = pushbacks.contains(uuid)

    fun add(uuid: UUID) {
        pushbacks.add(uuid)
    }

    fun remove(uuid: UUID) {
        pushbacks.remove(uuid)
    }
}