@file:Suppress("UnstableApiUsage")

package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.lobbyConfig
import io.papermc.paper.event.player.AsyncPlayerSpawnLocationEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object SpawnLocationListener : Listener {
    @EventHandler
    fun onConnect(event: AsyncPlayerSpawnLocationEvent) {
        event.spawnLocation = lobbyConfig.spawnPoint.toLocation()
    }
}