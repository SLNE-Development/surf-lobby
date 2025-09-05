package dev.slne.surf.lobby.paper.lobby.features.pushback

import dev.slne.surf.lobby.paper.common.utils.PermissionRegistry
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.springframework.stereotype.Component

@Component
class PushbackListener : Listener {

    private val pushbackForce = 2.0

    @EventHandler(ignoreCancelled = true)
    fun onAttack(event: PrePlayerAttackEntityEvent) {
        val player = event.player
        val attacked = event.attacked as? Player ?: return

        event.isCancelled = true

        if (!player.isSneaking) {
            return
        }

        if (!player.hasPermission(PermissionRegistry.PUSHBACK_ATTACK)) {
            return
        }

        attacked.velocity = player.eyeLocation.direction.multiply(pushbackForce)
    }

}