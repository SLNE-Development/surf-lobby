package dev.slne.surf.lobby.profile

import dev.slne.surf.profile.api.surfProfileApi
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ProfileHook {
    fun isEnabled() = Bukkit.getPluginManager().isPluginEnabled("surf-profile-paper")

    fun openMenu(player: Player) {
        if (isEnabled()) {
            surfProfileApi.openOwnProfileMenu(player.uniqueId)
        } else {
            player.sendText {
                appendPrefix()
                error("Lobby: Internal Server error while handling profile hook. Is everything loaded correctly?")
            }
        }
    }
}