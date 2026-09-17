package dev.slne.surf.lobby.core.client.event

import dev.slne.surf.api.core.util.runAtFixedRate
import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.event.data.EventDataSource
import dev.slne.surf.lobby.core.client.redis.lobbyRedisApi
import dev.slne.surf.redis.sync.value.SyncValue
import dev.slne.surf.redis.sync.value.SyncValueChange
import kotlinx.coroutines.CoroutineScope
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import kotlin.time.Duration.Companion.minutes

object EventServerBridge {
    private val logger = ComponentLogger.logger("surf-lobby")

    @Volatile
    lateinit var state: SyncValue<EventServerState>

    fun init(scope: CoroutineScope) {
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

        scope.runAtFixedRate(5.minutes) {
            EventDataSource.refreshCache()
        }
    }
}
