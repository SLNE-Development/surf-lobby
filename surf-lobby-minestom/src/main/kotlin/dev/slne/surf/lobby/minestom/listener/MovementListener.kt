package dev.slne.surf.lobby.minestom.listener

import dev.slne.surf.api.minestom.event.EventRegistrar
import dev.slne.surf.api.minestom.extension.addListener
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.jump.DoubleJumpTracker
import dev.slne.surf.lobby.minestom.elytra.ElytraBoostHandler
import dev.slne.surf.lobby.minestom.location.toPos
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerMoveEvent

object MovementListener : EventRegistrar {
    override fun register(node: EventNode<Event>) {
        node.addListener<PlayerMoveEvent> { event ->
            val player = event.player
            val minHeight = lobbyConfig.minHeight

            if (event.newPosition.y < minHeight) {
                player.teleport(lobbyConfig.spawnPoint.toPos()).thenRun {
                    player.velocity = player.velocity.withY(0.0)
                }
                return@addListener
            }

            if (player.isOnGround) {
                DoubleJumpTracker.onGrounded(player.uuid)
                ElytraBoostHandler.clearBoost(player)
            }
        }
    }
}
