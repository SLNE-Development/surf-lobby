package dev.slne.surf.lobby.hook.parkour

import dev.slne.surf.parkour.api.SurfParkourApi
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ParkourHook {
    fun isEnabled() = Bukkit.getPluginManager().isPluginEnabled("surf-parkour-paper")

    fun isInParkour(player: Player) =
        if (isEnabled()) SurfParkourApi.isInParkour(player.uniqueId) else false

    suspend fun openParkourGui(player: Player) {
        if (isEnabled()) {
            SurfParkourApi.showGui(player.uniqueId)
        } else {
            player.sendText {
                appendErrorPrefix()
                error("Lobby: Internal Server error while handling parkour hook. Is everything loaded correctly?")
            }
        }
    }
}