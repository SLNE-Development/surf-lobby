package dev.slne.surf.lobby.minestom.listener

import dev.slne.surf.api.minestom.event.EventRegistrar
import dev.slne.surf.api.minestom.extension.addListener
import dev.slne.surf.api.minestom.permission.hasPermission
import dev.slne.surf.lobby.core.client.pushback.PushbackStates
import dev.slne.surf.lobby.core.client.util.yearProgress
import dev.slne.surf.lobby.minestom.elytra.ElytraBoostHandler
import dev.slne.surf.lobby.minestom.item.HotbarItem
import dev.slne.surf.lobby.minestom.visibility.PlayerVisibilityService
import net.minestom.server.entity.EquipmentSlot
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.item.ItemStack
import java.time.LocalDate

object LobbyPlayerListener : EventRegistrar {
    override fun register(node: EventNode<Event>) {
        node.addListener<PlayerSpawnEvent> { event ->
            if (!event.isFirstSpawn) return@addListener

            val player = event.player
            val inventory = player.inventory

            player.setHeldItemSlot(4)

            calcYearXp(player)

            for (i in 0..8) {
                inventory.setItemStack(i, ItemStack.AIR)
            }

            player.setEquipment(EquipmentSlot.CHESTPLATE, ItemStack.AIR)

            for (item in HotbarItem.bySlot.values) {
                val permission = item.permission

                if (permission == null || player.hasPermission(permission)) {
                    inventory.setItemStack(item.slot, item.getItemForPlayer(player))
                }
            }

            PlayerVisibilityService.onPlayerJoin(player)
        }

        node.addListener<PlayerDisconnectEvent> { event ->
            PushbackStates.remove(event.player.uuid)
            PlayerVisibilityService.remove(event.player.uuid)
            ElytraBoostHandler.clearBoost(event.player)
        }
    }

    private fun calcYearXp(player: Player) {
        val today: LocalDate = LocalDate.now()

        player.exp = yearProgress(today)
        player.level = today.year
    }
}
