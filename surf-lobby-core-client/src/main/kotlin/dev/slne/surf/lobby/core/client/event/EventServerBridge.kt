package dev.slne.surf.lobby.core.client.event

import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.core.client.redis.lobbyRedisApi
import dev.slne.surf.redis.sync.value.SyncValue
import dev.slne.surf.redis.sync.value.SyncValueChange
import net.kyori.adventure.text.logger.slf4j.ComponentLogger

val eventServerBridge = EventServerBridge()

class EventServerBridge {
    private val logger = ComponentLogger.logger("surf-lobby")

    lateinit var state: SyncValue<EventServerState>
    fun init() {
        state = lobbyRedisApi.createSyncValue(
            "surf-event:event-server-state",
            EventServerState.CLOSED
        )

        state.addListener { change ->
            when (change) {
                is SyncValueChange.Updated<*> -> {
                    val change = change.new as? EventServerState
                        ?: error("Received SyncValue update with invalid event server state")

                    logger.info("Event Server state changed to ${change.displayName}")
                }
            }
        }
    }
}
