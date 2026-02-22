package dev.slne.surf.lobby.manager

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.lobby.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType
import java.util.*

@Suppress("UnstableApiUsage")
object ElytraBoostManager {
    val boostingPlayers = mutableObjectSetOf<UUID>()

    fun checkAndBoost(player: Player) {
        if (boostingPlayers.contains(player.uniqueId)) {
            return
        }

        if (player.isGliding) {
            return
        }

        boostingPlayers.add(player.uniqueId)

        player.location.world.spawnParticle(Particle.CLOUD, player.location, 25, 0.5, 0.0, 0.5, 0.1)
        player.playSound(true) {
            type(Sound.ENTITY_EGG_THROW)
        }

        plugin.launch(plugin.entityDispatcher(player)) {
            player.inventory.chestplate = ItemType.ELYTRA.createItemStack()
            player.isGliding = true

            val direction = player.location.direction.normalize()
            val boostedDirection = direction.add(org.bukkit.util.Vector(0.0, 0.4, 0.0)).normalize()

            player.velocity = boostedDirection.multiply(1.4)
        }
    }

    fun clearBoost(player: Player) {
        if (boostingPlayers.remove(player.uniqueId)) {
            plugin.launch(plugin.entityDispatcher(player)) {
                player.inventory.chestplate = null
            }
        }
    }
}