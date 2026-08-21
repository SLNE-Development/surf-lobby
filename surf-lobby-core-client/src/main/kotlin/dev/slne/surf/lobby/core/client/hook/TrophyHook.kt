package dev.slne.surf.lobby.core.client.hook

import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.trophy.api.SurfTrophyApi
import java.util.*

object TrophyHook {
    val available get() = LobbyPlatform.trophyAvailable

    fun openMenu(playerUuid: UUID) {
        SurfTrophyApi.showTrophyMenu(playerUuid, playerUuid)
    }
}
