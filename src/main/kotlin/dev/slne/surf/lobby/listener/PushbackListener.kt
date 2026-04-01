package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.manager.PushbackManager
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent
import org.bukkit.Effect
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.BoundingBox

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

    @EventHandler
    fun handleHitbox(event: PlayerMoveEvent) {
        val player = event.player

        if (!event.hasExplicitlyChangedBlock()) {
            return
        }

        val range = PushbackManager.RANGE

        if (PushbackManager.hasPushback(player.uniqueId)) {
            PushbackManager.hitboxes[player.uniqueId] = BoundingBox(
                player.location.x - range, player.location.y - range, player.location.z - range,
                player.location.x + range, player.location.y + range, player.location.z + range
            )
        }
    }

    @EventHandler
    fun handlePushback(event: PlayerMoveEvent) {
        val player = event.player

        if (!event.hasExplicitlyChangedBlock()) {
            return
        }

        if (event.player.hasPermission(PermissionRegistry.PUSHBACK_ITEM)) {
            return
        }

        for ((uuid, hitbox) in PushbackManager.hitboxes) {
            if (hitbox.contains(player.location.toVector())) {
                val pushbackPlayer = player.server.getPlayer(uuid) ?: continue

                player.velocity = pushbackPlayer.location.toVector()
                    .subtract(player.location.toVector())
                    .multiply(-0.5)
                    .setY(0.5)

                pushbackPlayer.world.playEffect(pushbackPlayer.location, Effect.ENDER_SIGNAL, null)
            }
        }
    }
}
