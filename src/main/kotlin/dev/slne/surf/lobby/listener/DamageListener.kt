@file:Suppress("UnstableApiUsage")

package dev.slne.surf.lobby.listener

import dev.slne.surf.surfapi.bukkit.api.event.cancel
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

object DamageListener : Listener {
    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            event.cancel()
        }
    }
}