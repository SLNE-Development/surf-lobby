package dev.slne.surf.lobby.minestom.platform

import com.google.auto.service.AutoService
import dev.slne.surf.api.minestom.coroutine.minestomScope
import dev.slne.surf.api.minestom.extension.ConnectionManager
import dev.slne.surf.api.minestom.permission.hasPermission
import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.lobby.minestom.LobbyMinestomPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@AutoService(LobbyPlatform::class)
class MinestomLobbyPlatform : LobbyPlatform {

    override val dataPath get() = LobbyMinestomPlugin.dataDirectory

    override val defaultWorldName: String get() = "lobby"

    override val parkourAvailable = true
    override val trophyAvailable = true
    override val profileAvailable = true
    override val settingsAvailable = true

    override fun hasPermission(playerUuid: UUID, permission: String) =
        ConnectionManager.getOnlinePlayerByUuid(playerUuid)?.hasPermission(permission) == true

    override fun audience(playerUuid: UUID) = ConnectionManager.getOnlinePlayerByUuid(playerUuid)

    override fun launch(block: suspend CoroutineScope.() -> Unit) =
        minestomScope.launch(block = block)
}
