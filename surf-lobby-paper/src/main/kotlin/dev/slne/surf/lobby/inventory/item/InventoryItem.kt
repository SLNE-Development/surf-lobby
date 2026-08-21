package dev.slne.surf.lobby.inventory.item

import dev.slne.surf.api.core.util.freeze
import dev.slne.surf.lobby.inventory.item.impl.navigator.NavigatorItem
import dev.slne.surf.lobby.inventory.item.impl.parkour.ParkourItem
import dev.slne.surf.lobby.inventory.item.impl.profile.ProfileItem
import dev.slne.surf.lobby.inventory.item.impl.pushback.PushbackDisableInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.pushback.PushbackEnableInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.rewards.TrophiesItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowAllPlayersInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowNonePlayersInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowTeamPlayersInventoryItem
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class InventoryItem(
    val slot: Int,
    val item: ItemStack
) {
    abstract val permission: String?
    abstract fun onInteract(player: Player)

    /**
     * Gets the item for a specific player. Override this method to provide player-specific items.
     * By default, returns the static item.
     */
    open fun getItemForPlayer(player: Player): ItemStack = item

    companion object {
        val items = listOf(
            PushbackDisableInventoryItem,
            PushbackEnableInventoryItem,
            NavigatorItem,
            ShowNonePlayersInventoryItem,
            ShowTeamPlayersInventoryItem,
            ShowAllPlayersInventoryItem,
            ProfileItem,
            ParkourItem,
            TrophiesItem
        ).associateByTo(Object2ObjectOpenHashMap()) { it.slot }.freeze()
    }
}