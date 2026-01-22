package dev.slne.surf.lobby.hook.settings

import dev.slne.surf.settings.api.surfSettingsApi
import org.bukkit.Bukkit
import java.util.*

object SettingsHook {
    fun isEnabled() = Bukkit.getPluginManager().isPluginEnabled("surf-settings-paper")

    fun hasScrollSoundsEnabled(playerUuid: UUID): Boolean {
        return if (isEnabled()) {
            surfSettingsApi.getPlayerSetting(playerUuid, "lobby_scroll_sound")?.getBoolean()
                ?: false
        } else {
            false
        }
    }
}