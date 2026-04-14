package dev.slne.surf.lobby.manager

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.core.util.mutableObject2ObjectMapOf
import dev.slne.surf.api.core.util.mutableObjectSetOf
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.meta
import dev.slne.surf.lobby.hook.parkour.ParkourHook
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.PermissionRegistry
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType
import java.util.*

@Suppress("UnstableApiUsage")
object ElytraBoostManager {
    val boostingPlayers = mutableObjectSetOf<UUID>()
    val lastBoosted = mutableObject2ObjectMapOf<UUID, Long>()

    private const val BOOST_COOLDOWN = 2000L

    fun checkAndBoost(player: Player) {
        if (!player.hasPermission(PermissionRegistry.ELYTRA_BOOST)) {
            return
        }

        if (plugin.checkParkourHook() && ParkourHook.isInParkour(player)) {
            return
        }

        if (boostingPlayers.contains(player.uniqueId)) {
            boostFlight(player)
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

        lastBoosted[player.uniqueId] = System.currentTimeMillis()

        plugin.launch(plugin.entityDispatcher(player)) {
            player.inventory.setChestplate(elytraItem)
            player.isGliding = true

            val direction = player.location.direction.normalize()
            player.velocity = direction.multiply(2)
        }
    }


    fun boostFlight(player: Player) {
        if (!boostingPlayers.contains(player.uniqueId)) {
            return
        }

        if (lastBoosted[player.uniqueId]?.let { System.currentTimeMillis() - it < BOOST_COOLDOWN } == true) {
            return
        }

        player.startRiptideAttack(20, 1.0f, null)
        player.location.world.spawnParticle(Particle.CLOUD, player.location, 25, 0.5, 0.0, 0.5, 0.1)
        player.playSound(true) {
            type(Sound.ITEM_TRIDENT_RIPTIDE_1)
        }

        val direction = player.location.direction.normalize()
        player.velocity = direction.multiply(2.5)

        lastBoosted[player.uniqueId] = System.currentTimeMillis()
    }

    fun clearBoost(player: Player) {
        if (boostingPlayers.remove(player.uniqueId)) {
            plugin.launch(plugin.entityDispatcher(player)) {
                player.inventory.setChestplate(null)
            }

            lastBoosted.remove(player.uniqueId)
        }
    }

    private val elytraItem by lazy {
        ItemType.ELYTRA.createItemStack().apply {
            displayName {
                variableValue("Elytra Boost")
            }

            meta {
                isUnbreakable = true
            }
        }
    }
}