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
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class InventoryItem(
    val slot: Int,
    val item: ItemStack
) {
    abstract val permission: String?
    abstract fun onInteract(player: Player)

    /**
     * Whether a joining player starts with this item in [slot].
     */
    open val placedOnJoin: Boolean get() = true

    /**
     * Gets the item for a specific player. Override this method to provide player-specific items.
     * By default, returns the static item.
     *
     * An override must keep the item type of [item]: `ItemInteractListener` rules a candidate out
     * by comparing types before it asks for the player-specific item, so that it does not have to
     * build one for every item on every interaction.
     */
    open fun getItemForPlayer(player: Player): ItemStack = item

    companion object {
        /**
         * Every hotbar item, including the ones that only appear once a player toggles a slot.
         */
        val all: List<InventoryItem> = listOf(
            PushbackDisableInventoryItem,
            PushbackEnableInventoryItem,
            NavigatorItem,
            ShowNonePlayersInventoryItem,
            ShowTeamPlayersInventoryItem,
            ShowAllPlayersInventoryItem,
            ProfileItem,
            ParkourItem,
            TrophiesItem
        )

        /**
         * The item each slot starts with, keyed by slot.
         */
        val bySlot: Int2ObjectMap<InventoryItem> =
            Int2ObjectOpenHashMap<InventoryItem>(all.size).apply {
                for (item in all) {
                    if (!item.placedOnJoin) {
                        continue
                    }

                    val clash = put(item.slot, item)

                    require(clash == null) {
                        "Slot ${item.slot} is placed on join by both " +
                                "${clash?.javaClass?.simpleName} and ${item.javaClass.simpleName}"
                    }
                }
            }.freeze()
    }
}
