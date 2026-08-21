package dev.slne.surf.lobby.core.client.event

import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.lobby.core.client.config.lobbyConfig

/**
 * Keeps track of the display name of the event server, so the event NPC can follow renames.
 */
object EventServerDisplayName {
    var current: String = "Event"
        private set

    /**
     * Re-reads the display name of the event server and returns the new name when it changed
     * since the last call, or `null` while it stayed the same or the server is unknown.
     */
    fun refresh(): String? {
        val server = SurfCoreApi.getServerByName(lobbyConfig.eventServerName) ?: return null

        if (current != server.displayName) {
            current = server.displayName
            return current
        }

        return null
    }
}
