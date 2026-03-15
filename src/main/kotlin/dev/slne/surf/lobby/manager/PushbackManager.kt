package dev.slne.surf.lobby.manager

import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.util.toPlayers
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import org.bukkit.Bukkit
import org.bukkit.Effect
import java.util.*

object PushbackManager {
    private val pushbacks = mutableObjectSetOf<UUID>()
    private const val RANGE = 3.0
    private const val FORCE = -0.5
    private const val Y_FORCE = 0.5

    fun startTask() {
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, {
            pushbacks.toPlayers().forEach { player ->
                val nearbyPlayers = player.location.getNearbyPlayers(RANGE) { other ->
                    other != player && !other.hasPermission(PermissionRegistry.PUSHBACK_ITEM)
                }

                for (nearby in nearbyPlayers) {
                    nearby.velocity = player.location.toVector()
                        .subtract(nearby.location.toVector())
                        .multiply(FORCE)
                        .setY(Y_FORCE)
                }

                player.world.playEffect(player.location, Effect.ENDER_SIGNAL, null)
            }
        }, 10, 10)
    }

    fun add(uuid: UUID) {
        pushbacks.add(uuid)
    }

    fun remove(uuid: UUID) {
        pushbacks.remove(uuid)
    }
}