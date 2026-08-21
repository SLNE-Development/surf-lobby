package dev.slne.surf.lobby.listener

import dev.slne.surf.lobby.core.client.util.yearProgress
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.ElytraBoostManager
import dev.slne.surf.lobby.manager.PlayerVisibilityManager
import dev.slne.surf.lobby.manager.PushbackManager
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.time.LocalDate

object PlayerConnectionListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.gameMode = GameMode.ADVENTURE
        event.player.inventory.heldItemSlot = 4

        calcYearXp(event.player)

        for (i in 0..8) {
            event.player.inventory.clear(i)
        }

        event.player.inventory.setChestplate(null)

        InventoryItem.items.values.filter { item ->
            item.permission?.let { event.player.hasPermission(it) } ?: true
        }.forEach {
            event.player.inventory.setItem(it.slot, it.getItemForPlayer(event.player))
        }

        PlayerVisibilityManager.onPlayerJoin(event.player)
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent) {
        PushbackManager.remove(event.player.uniqueId)
        PlayerVisibilityManager.remove(event.player.uniqueId)
        ElytraBoostManager.clearBoost(event.player)
    }

    private fun calcYearXp(player: Player) {
        val today: LocalDate = LocalDate.now()

        player.exp = yearProgress(today)
        player.level = today.year
    }
}
