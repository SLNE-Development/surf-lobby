package dev.slne.surf.lobby.core.client.event

import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import java.util.concurrent.atomic.AtomicReference

/**
 * Keeps track of the display name of the event server, so the event NPC can follow renames.
 */
object EventServerDisplayName {
    private val currentName = AtomicReference("Event")

    val current: String get() = currentName.get()

    /**
     * Re-reads the display name of the event server and returns the new name when it changed
     * since the last call, or `null` while it stayed the same or the server is unknown.
     */
    fun refresh(): String? {
        val displayName =
            SurfCoreApi.getServerByName(lobbyConfig.eventServerName)?.displayName ?: return null

        return displayName.takeIf { currentName.getAndSet(it) != it }
    }
}
