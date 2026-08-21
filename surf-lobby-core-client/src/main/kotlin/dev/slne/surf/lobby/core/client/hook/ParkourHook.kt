package dev.slne.surf.lobby.core.client.hook

import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.parkour.api.SurfParkourApi
import java.util.*

object ParkourHook {
    val available get() = LobbyPlatform.parkourAvailable

    fun isInParkour(playerUuid: UUID) = SurfParkourApi.isInParkour(playerUuid)

    suspend fun openParkourGui(playerUuid: UUID) = SurfParkourApi.showGui(playerUuid)
}
