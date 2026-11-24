package dev.slne.surf.lobby.core.client.hook

import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.player.api.settings.SurfSettingsApi
import dev.slne.surf.player.api.settings.setting.SettingKeys
import java.util.*

object SettingsHook {
    val available get() = LobbyPlatform.settingsAvailable

    fun hasScrollSoundsEnabled(playerUuid: UUID): Boolean =
        SurfSettingsApi.getSettingValue(playerUuid, SettingKeys.LOBBY_SCROLL_SOUND)
}
