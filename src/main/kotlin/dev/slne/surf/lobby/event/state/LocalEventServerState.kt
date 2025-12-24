package dev.slne.surf.lobby.event.state

import dev.slne.surf.event.base.api.common.state.EventServerState

enum class LocalEventServerState {
    OPEN,
    CLOSED,
    UNKNOWN;

    companion object {
        fun ofState(eventServerState: EventServerState): LocalEventServerState {
            return when (eventServerState) {
                EventServerState.OPEN -> OPEN
                EventServerState.CLOSED -> CLOSED
            }
        }
    }
}