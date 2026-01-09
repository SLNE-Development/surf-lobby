package dev.slne.surf.lobby.trophy

import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.trophy.api.surfTrophyApi
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object TrophyHook {
    fun isEnabled() = Bukkit.getPluginManager().isPluginEnabled("surf-trophy-paper")

    fun openMenu(player: Player) {
        if (isEnabled()) {
            surfTrophyApi.showTrophyMenu(player, player)
        } else {
            player.sendText {
                appendPrefix()
                error("Lobby: Internal Server error while handling trophy hook. Is everything loaded correctly?")
            }
        }
    }
}