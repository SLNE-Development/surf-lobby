package dev.slne.surf.lobby.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

object EntitySpawnListener : Listener {
    @EventHandler
    fun onSpawn(event: CreatureSpawnEvent) {
        event.isCancelled = true
    }
}