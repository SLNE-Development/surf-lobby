package dev.slne.surf.lobby.hook.trophy

import dev.slne.surf.trophy.api.surfTrophyApi
import org.bukkit.entity.Player

object TrophyHook {
    fun openMenu(player: Player) {
        surfTrophyApi.showTrophyMenu(player.uniqueId, player.uniqueId)
    }
}