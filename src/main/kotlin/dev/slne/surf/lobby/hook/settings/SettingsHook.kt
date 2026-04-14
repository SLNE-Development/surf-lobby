package dev.slne.surf.lobby.hook.settings

import dev.slne.surf.settings.api.SurfSettingsApi
import java.util.*

object SettingsHook {
    fun hasScrollSoundsEnabled(playerUuid: UUID): Boolean {
        return SurfSettingsApi.getPlayerSetting(playerUuid, "lobby_scroll_sound")?.getBoolean()
            ?: false
    }
}