package dev.slne.surf.lobby.paper.lobby.features.pushback

import dev.slne.surf.cloud.api.common.util.mutableObjectListOf
import dev.slne.surf.lobby.paper.common.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.extensions.server
import org.bukkit.Effect
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

private const val RANGE = 3.0
private const val FORCE = -0.5
private const val Y_FORCE = 0.5

@Component
class PushbackManager : Listener {
    
    val pushback = mutableObjectListOf<UUID>()

    @Scheduled(fixedDelay = 100)
    fun applyPushback() {
        pushback.mapNotNull { server.getPlayer(it) }.forEach { player ->
            val nearbyPlayers = player.getNearbyEntities(RANGE, RANGE, RANGE)
                .filterNot { it == player }
                .filterIsInstance<Player>()
                .filterNot { it.hasPermission(PermissionRegistry.PUSHBACK_ITEM) }

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
        pushback.remove(event.player.uniqueId)
    }

}