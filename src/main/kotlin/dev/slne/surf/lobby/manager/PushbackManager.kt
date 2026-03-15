package dev.slne.surf.lobby.manager

import dev.slne.surf.lobby.utils.CircularBoundingBox
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.entity.Player
import java.util.*

object PushbackManager {
    private val boundingBoxes = mutableObject2ObjectMapOf<UUID, CircularBoundingBox>()
    private val lastPushbackTimes = mutableObject2ObjectMapOf<UUID, Long>()
    private const val RANGE = 3.0
    private const val FORCE = -0.5
    private const val Y_FORCE = 0.5
    private const val COOLDOWN_MS = 1000L

    fun add(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return
        val loc = player.location
        boundingBoxes[uuid] = CircularBoundingBox(loc.x, loc.z, RANGE)
    }

    fun remove(uuid: UUID) {
        boundingBoxes.remove(uuid)
        lastPushbackTimes.remove(uuid)
    }

    fun updateExecutorPosition(player: Player) {
        val box = boundingBoxes[player.uniqueId] ?: return
        val loc = player.location
        box.updateCenter(loc.x, loc.z)
    }

    fun checkPushback(player: Player) {
        if (player.hasPermission(PermissionRegistry.PUSHBACK_ITEM)) return

        val now = System.currentTimeMillis()
        val lastPushback = lastPushbackTimes[player.uniqueId]
        if (lastPushback != null && now - lastPushback < COOLDOWN_MS) return

        val playerLoc = player.location

        for ((executorUuid, box) in boundingBoxes) {
            if (executorUuid == player.uniqueId) continue
            if (!box.isInside(playerLoc.x, playerLoc.z)) continue

            val executor = Bukkit.getPlayer(executorUuid) ?: continue
            if (executor.world != player.world) continue

            player.velocity = executor.location.toVector()
                .subtract(playerLoc.toVector())
                .multiply(FORCE)
                .setY(Y_FORCE)

            executor.world.playEffect(executor.location, Effect.ENDER_SIGNAL, null)
            lastPushbackTimes[player.uniqueId] = now
            break
        }
    }
}