package dev.slne.surf.lobby.core.client.hook

import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.player.api.profile.SurfProfileApi
import java.util.*

object ProfileHook {
    val available get() = LobbyPlatform.profileAvailable

    fun openMenu(playerUuid: UUID) {
        SurfProfileApi.openOwnProfileMenu(playerUuid)
    }
}
