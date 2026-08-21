package dev.slne.surf.lobby.minestom.platform

import com.google.auto.service.AutoService
import dev.slne.minestom.lobby.api.coroutine.minestomScope
import dev.slne.minestom.lobby.api.extension.ConnectionManager
import dev.slne.minestom.lobby.api.player.getLobbyPlayer
import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.lobby.minestom.LobbyMinestomEntrypoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@AutoService(LobbyPlatform::class)
class MinestomLobbyPlatform : LobbyPlatform {

    override val dataPath get() = LobbyMinestomEntrypoint.dataPath

    override val defaultWorldName: String get() = "lobby"

    override val parkourAvailable = true
    override val trophyAvailable = true
    override val profileAvailable = true
    override val settingsAvailable = true

    override fun hasPermission(playerUuid: UUID, permission: String) =
        ConnectionManager.getLobbyPlayer(playerUuid)?.hasPermission(permission) == true

    override fun audience(playerUuid: UUID) = ConnectionManager.getLobbyPlayer(playerUuid)

    override fun launch(block: suspend CoroutineScope.() -> Unit) =
        minestomScope.launch(block = block)
}
