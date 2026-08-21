package dev.slne.surf.lobby.core.client.hook

import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.profile.api.surfProfileApi
import java.util.*

object ProfileHook {
    val available get() = LobbyPlatform.profileAvailable

    fun openMenu(playerUuid: UUID) {
        surfProfileApi.openOwnProfileMenu(playerUuid)
    }
}
