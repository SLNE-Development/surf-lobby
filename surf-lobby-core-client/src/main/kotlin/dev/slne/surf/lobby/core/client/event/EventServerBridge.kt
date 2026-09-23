package dev.slne.surf.lobby.core.client.event

import dev.slne.surf.api.core.util.runAtFixedRate
import dev.slne.surf.event.data.EventDataSource
import dev.slne.surf.event.state.EventServerStateAccess
import dev.slne.surf.lobby.core.client.redis.lobbyRedisApi
import kotlinx.coroutines.CoroutineScope
import kotlin.time.Duration.Companion.minutes

object EventServerBridge {

    @Volatile
    lateinit var state: EventServerStateAccess
        private set

    fun init(scope: CoroutineScope) {
        state = EventServerStateAccess(lobbyRedisApi)

        scope.runAtFixedRate(5.minutes) {
            EventDataSource.refreshCache()
        }
    }
}
