package dev.slne.surf.lobby.core.client.visibility

import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerVisibilityStates {
    private val visibilityStates = ConcurrentHashMap<UUID, VisibilityState>()

    enum class VisibilityState {
        SHOW_ALL,
        SHOW_TEAM,
        SHOW_NONE
    }

    fun getState(uuid: UUID): VisibilityState {
        return visibilityStates.getOrDefault(uuid, VisibilityState.SHOW_ALL)
    }

    fun setState(uuid: UUID, state: VisibilityState) {
        visibilityStates[uuid] = state
    }

    fun remove(uuid: UUID) {
        visibilityStates.remove(uuid)
    }
}
