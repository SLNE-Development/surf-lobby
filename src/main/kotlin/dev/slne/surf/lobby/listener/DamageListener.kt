@file:Suppress("UnstableApiUsage")

package dev.slne.surf.lobby.listener

import dev.slne.surf.surfapi.bukkit.api.event.cancel
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent

object DamageListener : Listener {
    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.entity is Player) {
            event.cancel()
        }
    }

    @EventHandler
    fun onDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager is Player) {
            event.cancel()
        }
    }

    @EventHandler
    fun onFoodLose(event: FoodLevelChangeEvent) {
        if (event.entity is Player) {
            event.cancel()
        }
    }
}