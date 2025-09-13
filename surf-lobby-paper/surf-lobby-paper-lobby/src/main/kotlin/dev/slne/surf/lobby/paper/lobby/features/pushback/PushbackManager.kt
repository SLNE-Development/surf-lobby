package dev.slne.surf.lobby.paper.lobby.features.pushback

import dev.slne.surf.cloud.api.common.util.mapAsync
import dev.slne.surf.lobby.paper.common.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.util.toPlayers
import org.bukkit.Effect
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

private const val RANGE = 3.0
private const val FORCE = -0.5
private const val Y_FORCE = 0.5

@Component
class PushbackManager : Listener {

    private val pushbacks = ConcurrentHashMap.newKeySet<UUID>()

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    suspend fun applyPushback() {
        pushbacks.toPlayers().mapAsync { player ->
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
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        pushbacks.remove(event.player.uniqueId)
    }

    fun add(uuid: UUID) {
        pushbacks.add(uuid)
    }

    fun remove(uuid: UUID) {
        pushbacks.remove(uuid)
    }
}