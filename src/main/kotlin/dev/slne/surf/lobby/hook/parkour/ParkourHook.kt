package dev.slne.surf.lobby.hook.parkour

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import dev.slne.surf.lobby.plugin
import dev.slne.surf.parkour.api.SurfParkourApi
import kotlinx.coroutines.withContext
import org.bukkit.entity.Player

object ParkourHook {

    fun isInParkour(player: Player) = SurfParkourApi.isInParkour(player.uniqueId)

    suspend fun openParkourGui(player: Player) = withContext(plugin.entityDispatcher(player)) {
        SurfParkourApi.showGui(player.uniqueId)
    }
}