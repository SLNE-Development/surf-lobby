package dev.slne.surf.lobby.minestom.pushback

import dev.slne.minestom.lobby.api.extension.ConnectionManager
import dev.slne.minestom.lobby.api.extension.SchedulerManager
import dev.slne.minestom.lobby.api.player.LobbyPlayer
import dev.slne.minestom.lobby.api.player.getLobbyPlayer
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.pushback.PushbackStates
import dev.slne.surf.lobby.minestom.util.blocksPerTick
import net.minestom.server.network.packet.server.play.WorldEventPacket
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule
import net.minestom.server.worldevent.WorldEvent

object PushbackTask {
    private var task: Task? = null

    fun start() {
        task = SchedulerManager.scheduleTask({
            PushbackStates.all()
                .mapNotNull { ConnectionManager.getLobbyPlayer(it) }
                .forEach { player ->
                    val instance = player.instance ?: return@forEach

                    val nearbyPlayers = instance
                        .getNearbyEntities(player.position, PushbackStates.RANGE)
                        .filterIsInstance<LobbyPlayer>()
                        .filter { other ->
                            other != player
                                    && !other.hasPermission(LobbyPermissions.PUSHBACK_ITEM)
                        }

                    for (nearby in nearbyPlayers) {
                        nearby.velocity = player.position.sub(nearby.position)
                            .asVec()
                            .mul(PushbackStates.FORCE)
                            .withY(PushbackStates.Y_FORCE)
                            .blocksPerTick()
                    }

                    player.sendPacketToViewersAndSelf(
                        WorldEventPacket(
                            WorldEvent.PARTICLES_EYE_OF_ENDER_DEATH.id(),
                            player.position,
                            0,
                            false
                        )
                    )
                }
        }, TaskSchedule.tick(10), TaskSchedule.tick(10))
    }

    fun stop() {
        task?.cancel()
        task = null
    }
}
