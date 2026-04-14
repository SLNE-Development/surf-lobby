package dev.slne.surf.lobby.hook.profile

import dev.slne.surf.profile.api.surfProfileApi
import org.bukkit.entity.Player

object ProfileHook {
    fun openMenu(player: Player) {
        surfProfileApi.openOwnProfileMenu(player.uniqueId)
    }
}