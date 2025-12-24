package dev.slne.surf.lobby.event.listener

import dev.slne.surf.event.base.api.redis.event.EventServerStateChangeRedisEvent
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.event.state.LocalEventServerState
import dev.slne.surf.redis.event.OnRedisEvent

object EventServerStateChangeListener {
    @OnRedisEvent
    fun onEventServerStateChange(event: EventServerStateChangeRedisEvent) {
        eventServerBridge.state = LocalEventServerState.ofState(event.to)
    }
}