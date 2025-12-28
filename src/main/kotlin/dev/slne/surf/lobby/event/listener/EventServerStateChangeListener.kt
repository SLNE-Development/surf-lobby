package dev.slne.surf.lobby.event.listener

import dev.slne.surf.event.base.api.redis.event.EventServerStateChangeRedisEvent
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.event.state.LocalEventServerState
import dev.slne.surf.lobby.plugin
import dev.slne.surf.redis.event.OnRedisEvent

object EventServerStateChangeListener {
    @OnRedisEvent
    fun onEventServerStateChange(event: EventServerStateChangeRedisEvent) {
        plugin.logger.info("Event server state changed to ${event.to}")
        eventServerBridge.state = LocalEventServerState.ofState(event.to)
    }
}