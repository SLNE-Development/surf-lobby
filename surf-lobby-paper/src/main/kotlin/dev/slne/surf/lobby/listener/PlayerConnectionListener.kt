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
        val player = event.player
        val inventory = player.inventory

        player.gameMode = GameMode.ADVENTURE
        inventory.heldItemSlot = 4

        calcYearXp(player)

        for (i in 0..8) {
            inventory.clear(i)
        }

        inventory.setChestplate(null)

        for (item in InventoryItem.bySlot.values) {
            val permission = item.permission

            if (permission == null || player.hasPermission(permission)) {
                inventory.setItem(item.slot, item.getItemForPlayer(player))
            }
        }

        PlayerVisibilityManager.onPlayerJoin(player)
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
