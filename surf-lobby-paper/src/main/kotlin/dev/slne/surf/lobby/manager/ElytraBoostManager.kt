package dev.slne.surf.lobby.manager

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.meta
import dev.slne.surf.lobby.core.client.elytra.ElytraBoostTracker
import dev.slne.surf.lobby.core.client.hook.ParkourHook
import dev.slne.surf.lobby.core.client.item.LobbyItemContents
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.plugin
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

@Suppress("UnstableApiUsage")
object ElytraBoostManager {
    fun checkAndBoost(player: Player) {
        if (!player.hasPermission(LobbyPermissions.ELYTRA_BOOST)) {
            return
        }

        val uuid = player.uniqueId

        if (ParkourHook.available && ParkourHook.isInParkour(uuid)) {
            return
        }

        if (ElytraBoostTracker.isBoosting(uuid)) {
            boostFlight(player)
            return
        }

        if (player.isGliding) {
            return
        }

        ElytraBoostTracker.startBoosting(uuid)

        val location = player.location
        player.world.spawnParticle(Particle.CLOUD, location, 25, 0.5, 0.0, 0.5, 0.1)
        player.playSound(true) {
            type(Sound.ENTITY_EGG_THROW)
        }

        ElytraBoostTracker.markBoosted(uuid)

        plugin.launch(plugin.entityDispatcher(player)) {
            player.inventory.setChestplate(elytraItem)
            player.isGliding = true

            val direction = player.location.direction.normalize()
            player.velocity = direction.multiply(2)
        }
    }


    fun boostFlight(player: Player) {
        val uuid = player.uniqueId

        if (!ElytraBoostTracker.isBoosting(uuid)) {
            return
        }

        if (ElytraBoostTracker.isOnCooldown(uuid)) {
            return
        }

        player.startRiptideAttack(20, 1.0f, null)

        val location = player.location
        player.world.spawnParticle(Particle.CLOUD, location, 25, 0.5, 0.0, 0.5, 0.1)
        player.playSound(true) {
            type(Sound.ITEM_TRIDENT_RIPTIDE_1)
        }

        val direction = location.direction.normalize()
        player.velocity = direction.multiply(2.5)

        ElytraBoostTracker.markBoosted(uuid)
    }

    fun clearBoost(player: Player) {
        if (ElytraBoostTracker.clear(player.uniqueId)) {
            plugin.launch(plugin.entityDispatcher(player)) {
                player.inventory.setChestplate(null)
            }
        }
    }

    private val elytraItem by lazy {
        ItemType.ELYTRA.createItemStack().apply {
            displayName(LobbyItemContents.ElytraBoost.name)

            meta {
                isUnbreakable = true
            }
        }
    }
}
