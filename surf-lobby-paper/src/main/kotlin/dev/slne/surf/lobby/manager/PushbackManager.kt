package dev.slne.surf.lobby.manager

import dev.slne.surf.api.paper.util.toPlayers
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.pushback.PushbackStates
import dev.slne.surf.lobby.plugin
import org.bukkit.Bukkit
import org.bukkit.Effect
import java.util.*

object PushbackManager {
    fun startTask() {
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, {
            PushbackStates.all().toPlayers().forEach { player ->
                val location = player.location
                val nearbyPlayers = location.getNearbyPlayers(PushbackStates.RANGE) { other ->
                    other != player && !other.hasPermission(LobbyPermissions.PUSHBACK_ITEM)
                }

                for (nearby in nearbyPlayers) {
                    nearby.velocity = location.toVector()
                        .subtract(nearby.location.toVector())
                        .multiply(PushbackStates.FORCE)
                        .setY(PushbackStates.Y_FORCE)
                }

                player.world.playEffect(location, Effect.ENDER_SIGNAL, null)
            }
        }, 10, 10)
    }

    fun add(uuid: UUID) {
        PushbackStates.add(uuid)
    }

    fun remove(uuid: UUID) {
        PushbackStates.remove(uuid)
    }
}
