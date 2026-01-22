package dev.slne.surf.lobby.hook.parkour

import dev.slne.surf.parkour.api.surfParkourApi
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ParkourHook {
    fun isEnabled() = Bukkit.getPluginManager().isPluginEnabled("surf-parkour-paper")

    fun isInParkour(player: Player) = if (isEnabled()) surfParkourApi.isInParkour(player) else false
    suspend fun openParkourGui(player: Player) {
        if (isEnabled()) {
            surfParkourApi.showGui(player)
        } else {
            player.sendText {
                appendErrorPrefix()
                error("Lobby: Internal Server error while handling parkour hook. Is everything loaded correctly?")
            }
        }
    }
}