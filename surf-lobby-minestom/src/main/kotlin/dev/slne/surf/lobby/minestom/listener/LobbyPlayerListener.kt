package dev.slne.surf.lobby.minestom.listener

import com.google.inject.Inject
import dev.slne.minestom.lobby.api.event.EventRegistrar
import dev.slne.minestom.lobby.api.extension.addListener
import dev.slne.minestom.lobby.api.player.lobbyPlayer
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

class LobbyPlayerListener @Inject constructor() : EventRegistrar {
    override fun register(node: EventNode<Event>) {
        node.addListener<PlayerSpawnEvent> { event ->
            if (!event.isFirstSpawn) return@addListener

            val player = event.lobbyPlayer

            player.setHeldItemSlot(4)

            calcYearXp(player)

            for (i in 0..8) {
                player.inventory.setItemStack(i, ItemStack.AIR)
            }

            player.setEquipment(EquipmentSlot.CHESTPLATE, ItemStack.AIR)

            HotbarItem.items.values.filter { item ->
                item.permission?.let { player.hasPermission(it) } ?: true
            }.forEach {
                player.inventory.setItemStack(it.slot, it.getItemForPlayer(player))
            }

            PlayerVisibilityService.onPlayerJoin(player)
        }

        node.addListener<PlayerDisconnectEvent> { event ->
            PushbackStates.remove(event.player.uuid)
            PlayerVisibilityService.remove(event.player.uuid)
            ElytraBoostHandler.clearBoost(event.lobbyPlayer)
        }
    }

    private fun calcYearXp(player: Player) {
        val today: LocalDate = LocalDate.now()

        player.exp = yearProgress(today)
        player.level = today.year
    }
}
