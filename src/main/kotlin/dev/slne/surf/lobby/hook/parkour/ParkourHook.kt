package dev.slne.surf.lobby.hook.parkour

import dev.slne.surf.parkour.api.SurfParkourApi
import org.bukkit.entity.Player

object ParkourHook {

    fun isInParkour(player: Player) = SurfParkourApi.isInParkour(player.uniqueId)

    suspend fun openParkourGui(player: Player) {
        SurfParkourApi.showGui(player.uniqueId)
    }
}