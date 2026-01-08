package dev.slne.surf.lobby.inventory.item

import dev.slne.surf.lobby.inventory.item.impl.navigator.NavigatorItem
import dev.slne.surf.lobby.inventory.item.impl.parkour.ParkourItem
import dev.slne.surf.lobby.inventory.item.impl.profile.ProfileItem
import dev.slne.surf.lobby.inventory.item.impl.pushback.PushbackDisableInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.pushback.PushbackEnableInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.rewards.RewardsItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowAllPlayersInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowNonePlayersInventoryItem
import dev.slne.surf.lobby.inventory.item.impl.visibility.ShowTeamPlayersInventoryItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class InventoryItem(
    val slot: Int,
    val item: ItemStack
) {
    abstract val permission: String?
    abstract fun onInteract(player: Player)

    companion object {
        val items = mutableListOf<InventoryItem>()

        init {
            items.add(PushbackDisableInventoryItem)
            items.add(PushbackEnableInventoryItem)
            items.add(NavigatorItem)
            items.add(ShowNonePlayersInventoryItem)
            items.add(ShowTeamPlayersInventoryItem)
            items.add(ShowAllPlayersInventoryItem)

            items.add(ProfileItem)
            items.add(ParkourItem)
            items.add(RewardsItem)
        }
    }
}