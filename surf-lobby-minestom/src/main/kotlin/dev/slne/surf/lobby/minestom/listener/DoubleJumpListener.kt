package dev.slne.surf.lobby.minestom.listener

import com.google.inject.Inject
import dev.slne.minestom.lobby.api.event.EventRegistrar
import dev.slne.minestom.lobby.api.extension.addListener
import dev.slne.surf.lobby.core.client.hook.ParkourHook
import dev.slne.surf.lobby.core.client.jump.DoubleJumpTracker
import dev.slne.surf.lobby.minestom.util.blocksPerTick
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerInputEvent
import net.minestom.server.network.packet.server.play.ParticlePacket
import net.minestom.server.particle.Particle

class DoubleJumpListener @Inject constructor() : EventRegistrar {
    override fun register(node: EventNode<Event>) {
        node.addListener<PlayerInputEvent> { event ->
            val player = event.player
            val uuid = player.uuid

            if (!canUseDoubleJump(player)) {
                DoubleJumpTracker.clear(uuid)
                return@addListener
            }

            val fires = DoubleJumpTracker.onJumpInput(
                uuid,
                event.isHoldingJumpKey,
                player.isOnGround
            )
            if (!fires) return@addListener

            val direction = player.position.direction()
            player.velocity = direction.mul(2.0).withY(1.0).blocksPerTick()
            player.sendPacketToViewersAndSelf(
                ParticlePacket(Particle.EXPLOSION, player.position, Vec.ZERO, 0f, 10)
            )
        }

        node.addListener<PlayerDisconnectEvent> { event ->
            DoubleJumpTracker.clear(event.player.uuid)
        }
    }

    private fun canUseDoubleJump(player: Player): Boolean {
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) return false
        if (ParkourHook.available && ParkourHook.isInParkour(player.uuid)) return false
        return true
    }
}
