package dev.slne.surf.lobby.event

import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.plugin
import dev.slne.surf.redis.sync.value.SyncValue
import dev.slne.surf.redis.sync.value.SyncValueChange

val eventServerBridge = EventServerBridge()

class EventServerBridge {
    lateinit var state: SyncValue<EventServerState>
    fun init() {
        state = plugin.redisApi.createSyncValue(
            "surf-event:event-server-state",
            EventServerState.CLOSED
        )

        state.addListener { change ->
            when (change) {
                is SyncValueChange.Updated<*> -> {
                    val change = change.new as? EventServerState
                        ?: error("Received SyncValue update with invalid event server state")

                    plugin.logger.info("Event Server state changed to ${change.displayName}")
                }
            }
        }
    }
}