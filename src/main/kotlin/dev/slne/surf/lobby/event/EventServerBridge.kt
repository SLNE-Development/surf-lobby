package dev.slne.surf.lobby.event

import dev.slne.surf.event.base.api.redis.request.EventServerStateRequest
import dev.slne.surf.event.base.api.redis.response.EventServerStateResponse
import dev.slne.surf.lobby.event.state.LocalEventServerState
import dev.slne.surf.lobby.plugin

val eventServerBridge = EventServerBridge()

class EventServerBridge {
    var state: LocalEventServerState = LocalEventServerState.UNKNOWN

    suspend fun requestState() = runCatching {
        plugin.logger.info("Requesting event server state...")
        val response =
            plugin.redisApi.sendRequest<EventServerStateResponse>(EventServerStateRequest())
        state = LocalEventServerState.ofState(response.state)
        plugin.logger.info("Received event server state: $state")
    }.onFailure {
        state = LocalEventServerState.UNKNOWN
    }
}