package dev.slne.surf.lobby.minestom.listener

import dev.slne.surf.api.minestom.event.EventRegistrar
import dev.slne.surf.api.minestom.extension.addListener
import net.minestom.server.entity.GameMode
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.event.player.PlayerBlockPlaceEvent

object WorldProtectionListener : EventRegistrar {
    override fun register(node: EventNode<Event>) {
        node.addListener<PlayerBlockPlaceEvent> { event ->
            if (event.player.gameMode == GameMode.CREATIVE) return@addListener

            event.isCancelled = true
        }

        node.addListener<PlayerBlockBreakEvent> { event ->
            if (event.player.gameMode == GameMode.CREATIVE) return@addListener

            event.isCancelled = true
        }
    }
}
