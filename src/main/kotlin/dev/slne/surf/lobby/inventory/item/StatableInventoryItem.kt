package dev.slne.surf.lobby.inventory.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

/**
 * An inventory item that can have multiple states.
 * Each state has its own visual representation (ItemStack) and behavior.
 */
interface StatableInventoryItem<T : Enum<T>> : InventoryItem {
    /**
     * Gets the current state for a player.
     */
    fun getState(player: Player): T
    
    /**
     * Gets the ItemStack representation for a specific state.
     */
    fun getItemForState(state: T): ItemStack
    
    /**
     * Called when the player interacts with the item.
     * Should update the state and return the new state.
     */
    fun onInteractWithState(player: Player, currentState: T): T
    
    /**
     * Updates the player's inventory with the item for the current state.
     */
    fun updatePlayerItem(player: Player) {
        val state = getState(player)
        player.inventory.setItem(slot, getItemForState(state))
    }
    
    override val item: ItemStack
        get() = throw UnsupportedOperationException("Use getItemForState() instead")
    
    override fun onInteract(player: Player) {
        val currentState = getState(player)
        val newState = onInteractWithState(player, currentState)
        updatePlayerItem(player)
    }
}
