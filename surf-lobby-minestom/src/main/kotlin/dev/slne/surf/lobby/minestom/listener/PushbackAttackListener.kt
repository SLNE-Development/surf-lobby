package dev.slne.surf.lobby.minestom.listener

import com.google.inject.Inject
import dev.slne.minestom.lobby.api.event.EventRegistrar
import dev.slne.minestom.lobby.api.extension.addListener
import dev.slne.minestom.lobby.api.player.LobbyPlayer
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.minestom.util.blocksPerTick
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.entity.EntityAttackEvent
import net.minestom.server.sound.SoundEvent

class PushbackAttackListener @Inject constructor() : EventRegistrar {
    private companion object {
        const val PUSHBACK_FORCE = 2.0
    }

    override fun register(node: EventNode<Event>) {
        node.addListener<EntityAttackEvent> { event ->
            val player = event.entity as? LobbyPlayer ?: return@addListener
            val attacked = event.target as? LobbyPlayer ?: return@addListener

            if (!player.isSneaking) {
                return@addListener
            }

            if (!player.hasPermission(LobbyPermissions.PUSHBACK_ATTACK)) {
                return@addListener
            }

            attacked.velocity = player.position.direction()
                .mul(PUSHBACK_FORCE)
                .blocksPerTick()

            player.playSound(true) {
                type(SoundEvent.ENTITY_PLAYER_ATTACK_CRIT)
                volume(0.2f)
            }
        }
    }
}
