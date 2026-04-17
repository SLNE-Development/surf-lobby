package dev.slne.surf.lobby.hook.settings

import dev.slne.surf.settings.api.SurfSettingsApi
import dev.slne.surf.settings.api.setting.SettingKeys
import java.util.*

object SettingsHook {
    fun hasScrollSoundsEnabled(playerUuid: UUID): Boolean =
        SurfSettingsApi.getSettingValue(playerUuid, SettingKeys.LOBBY_SCROLL_SOUND)
}