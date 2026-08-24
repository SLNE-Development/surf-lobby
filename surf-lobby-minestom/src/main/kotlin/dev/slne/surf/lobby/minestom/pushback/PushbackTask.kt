package dev.slne.surf.lobby.minestom.pushback

import dev.slne.minestom.lobby.api.extension.ConnectionManager
import dev.slne.minestom.lobby.api.extension.SchedulerManager
import dev.slne.minestom.lobby.api.player.LobbyPlayer
import dev.slne.minestom.lobby.api.player.getLobbyPlayer
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.pushback.PushbackStates
import dev.slne.surf.lobby.minestom.util.blocksPerTick
import net.minestom.server.instance.EntityTracker
import net.minestom.server.network.packet.server.play.WorldEventPacket
import net.minestom.server.timer.Task
import net.minestom.server.timer.TaskSchedule
import net.minestom.server.worldevent.WorldEvent

object PushbackTask {
    @Volatile
    private var task: Task? = null

    fun start() {
        task = SchedulerManager.scheduleTask({
            for (uuid in PushbackStates.all()) {
                val player = ConnectionManager.getLobbyPlayer(uuid) ?: continue
                val instance = player.instance ?: continue
                val position = player.position

                instance.entityTracker.nearbyEntities(
                    position,
                    PushbackStates.RANGE,
                    EntityTracker.Target.PLAYERS,
                    fun(nearby) {
                        if (nearby !is LobbyPlayer ||
                            nearby == player ||
                            nearby.hasPermission(LobbyPermissions.PUSHBACK_ITEM)
                        ) {
                            return
                        }

                        nearby.velocity = position.sub(nearby.position)
                            .asVec()
                            .mul(PushbackStates.FORCE)
                            .withY(PushbackStates.Y_FORCE)
                            .blocksPerTick()
                    }
                )

                player.sendPacketToViewersAndSelf(
                    WorldEventPacket(
                        WorldEvent.PARTICLES_EYE_OF_ENDER_DEATH.id(),
                        position,
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
