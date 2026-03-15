package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object PushbackListener : Listener {
    private const val PUSHBACK_FORCE = 2.0

    @EventHandler
    fun onAttack(event: PrePlayerAttackEntityEvent) {
        val player = event.player
        val attacked = event.attacked as? Player ?: return

        if (!player.isSneaking) {
            return
        }

        if (!player.hasPermission(PermissionRegistry.PUSHBACK_ATTACK)) {
            return
        }

        event.cancel()
        attacked.velocity = player.eyeLocation.direction.multiply(PUSHBACK_FORCE)

        player.playSound(true) {
            type(Sound.ENTITY_PLAYER_ATTACK_CRIT)
            volume(0.2f)
        }
    }
}
