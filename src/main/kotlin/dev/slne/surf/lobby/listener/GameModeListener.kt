@file:Suppress("UnstableApiUsage")

package dev.slne.surf.lobby.listener

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object GameModeListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.gameMode = GameMode.ADVENTURE
    }
}