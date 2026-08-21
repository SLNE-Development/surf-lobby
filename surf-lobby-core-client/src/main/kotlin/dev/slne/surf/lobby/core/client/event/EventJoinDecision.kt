package dev.slne.surf.lobby.core.client.event

import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import java.util.*

/**
 * What happens when a player tries to join the event through an event NPC.
 */
enum class EventJoinAction {
    /**
     * An external event replaces the default one, so the player is teleported to its location.
     */
    EXTERNAL_TELEPORT,

    /**
     * The player is queued to the event server.
     */
    QUEUE,

    /**
     * The event server is closed and the player may not bypass that.
     */
    CLOSED_MESSAGE,

    /**
     * No event is running.
     */
    NO_EVENT_MESSAGE
}

/**
 * Decides what happens when the player identified by [playerUuid] tries to join the event.
 */
fun decideEventJoin(playerUuid: UUID): EventJoinAction {
    if (lobbyConfig.externalEventEnabled && lobbyConfig.externalEventReplacesDefault) {
        return EventJoinAction.EXTERNAL_TELEPORT
    }

    return when (eventServerBridge.state.get()) {
        EventServerState.OPEN -> EventJoinAction.QUEUE

        EventServerState.CLOSED ->
            if (LobbyPlatform.hasPermission(playerUuid, LobbyPermissions.EVENT_BYPASS)) {
                EventJoinAction.QUEUE
            } else {
                EventJoinAction.CLOSED_MESSAGE
            }

        EventServerState.UNKNOWN -> EventJoinAction.NO_EVENT_MESSAGE
    }
}
