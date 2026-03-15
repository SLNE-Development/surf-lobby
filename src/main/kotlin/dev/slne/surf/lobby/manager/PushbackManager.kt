package dev.slne.surf.lobby.manager

import dev.slne.surf.lobby.utils.CircularBoundingBox
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import org.bukkit.Bukkit
import org.bukkit.Effect
import org.bukkit.entity.Player
import java.util.*

object PushbackManager {
    private val pushbackZones = mutableObject2ObjectMapOf<UUID, CircularBoundingBox>()
    private val lastEffectTime = mutableObject2ObjectMapOf<UUID, Long>()
    private const val RANGE = 3.0
    private const val FORCE = -0.5
    private const val Y_FORCE = 0.5
    private const val EFFECT_COOLDOWN_MS = 1000L

    fun handlePlayerMove(player: Player) {
        val playerLocation = player.location

        pushbackZones[player.uniqueId]?.let { boundingBox ->
            boundingBox.center = playerLocation
        }

        if (player.hasPermission(PermissionRegistry.PUSHBACK_ITEM)) return

        for ((executorUuid, boundingBox) in pushbackZones) {
            if (executorUuid == player.uniqueId) continue
            if (!boundingBox.isInside(playerLocation)) continue

            val executor = Bukkit.getPlayer(executorUuid) ?: continue

            player.velocity = executor.location.toVector()
                .subtract(playerLocation.toVector())
                .multiply(FORCE)
                .setY(Y_FORCE)

            val now = System.currentTimeMillis()
            val lastTime = lastEffectTime.getOrDefault(executorUuid, 0L)
            if (now - lastTime >= EFFECT_COOLDOWN_MS) {
                executor.world.playEffect(executor.location, Effect.ENDER_SIGNAL, null)
                lastEffectTime[executorUuid] = now
            }
        }
    }

    fun add(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return
        pushbackZones[uuid] = CircularBoundingBox(player.location, RANGE)
    }

    fun remove(uuid: UUID) {
        pushbackZones.remove(uuid)
        lastEffectTime.remove(uuid)
    }
}