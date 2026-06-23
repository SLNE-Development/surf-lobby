package dev.slne.surf.lobby.hook.trophy

import dev.slne.surf.trophy.api.SurfTrophyApi
import org.bukkit.entity.Player

object TrophyHook {
    fun openMenu(player: Player) {
        SurfTrophyApi.showTrophyMenu(player.uniqueId, player.uniqueId)
    }
}