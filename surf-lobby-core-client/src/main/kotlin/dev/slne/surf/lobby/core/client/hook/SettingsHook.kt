package dev.slne.surf.lobby.core.client.hook

import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.settings.api.SurfSettingsApi
import dev.slne.surf.settings.api.setting.SettingKeys
import java.util.*

object SettingsHook {
    val available get() = LobbyPlatform.settingsAvailable

    fun hasScrollSoundsEnabled(playerUuid: UUID): Boolean =
        SurfSettingsApi.getSettingValue(playerUuid, SettingKeys.LOBBY_SCROLL_SOUND)
}
