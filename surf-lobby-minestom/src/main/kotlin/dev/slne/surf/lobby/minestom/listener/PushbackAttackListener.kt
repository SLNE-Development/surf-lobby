package dev.slne.surf.lobby.minestom.listener

import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.minestom.event.EventRegistrar
import dev.slne.surf.api.minestom.extension.addListener
import dev.slne.surf.api.minestom.permission.hasPermission
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.minestom.util.blocksPerTick
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.entity.EntityAttackEvent
import net.minestom.server.sound.SoundEvent

object PushbackAttackListener : EventRegistrar {
    private const val PUSHBACK_FORCE = 2.0

    override fun register(node: EventNode<Event>) {
        node.addListener<EntityAttackEvent> { event ->
            val player = event.entity as? Player ?: return@addListener
            val attacked = event.target as? Player ?: return@addListener

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
