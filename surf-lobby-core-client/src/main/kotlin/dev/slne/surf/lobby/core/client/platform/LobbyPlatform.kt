package dev.slne.surf.lobby.core.client.platform

import dev.slne.surf.api.core.util.requiredService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import net.kyori.adventure.audience.Audience
import java.nio.file.Path
import java.util.*

private val platform = requiredService<LobbyPlatform>()

/**
 * The platform-specific operations the shared lobby logic relies on.
 *
 * Every platform contributes exactly one implementation through `ServiceLoader`.
 */
interface LobbyPlatform {

    /**
     * The directory this plugin keeps its files in.
     */
    val dataPath: Path

    /**
     * The name of the world a newly written config falls back to.
     */
    val defaultWorldName: String

    /**
     * Whether the parkour integration is available on this server.
     */
    val parkourAvailable: Boolean

    /**
     * Whether the trophy integration is available on this server.
     */
    val trophyAvailable: Boolean

    /**
     * Whether the profile integration is available on this server.
     */
    val profileAvailable: Boolean

    /**
     * Whether the settings integration is available on this server.
     */
    val settingsAvailable: Boolean

    /**
     * Returns whether the player identified by [playerUuid] has [permission].
     */
    fun hasPermission(playerUuid: UUID, permission: String): Boolean

    /**
     * Returns the player identified by [playerUuid] as an audience, or `null` while they are not
     * connected to this server.
     */
    fun audience(playerUuid: UUID): Audience?

    /**
     * Launches [block] on the context the platform runs game logic on.
     */
    fun launch(block: suspend CoroutineScope.() -> Unit): Job

    companion object : LobbyPlatform by platform {
        val INSTANCE get() = platform
    }
}
