package dev.slne.surf.lobby.paper.lobby.inventory.item.items.state

import dev.slne.surf.lobby.paper.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.paper.lobby.inventory.item.InventoryItemAction
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class StateInventoryItem<H : StateHolder<S>, S : Statable>(
    slot: Int,
    itemStack: ItemStack,
    val stateHolder: H,
) : InventoryItem(slot, itemStack) {

    abstract fun onStateChange(player: Player, oldState: S, newState: S)

    override fun onClick(player: Player, action: InventoryItemAction) {
        val nextState = stateHolder.getNextState()

        onStateChange(player, stateHolder.state, nextState)
        stateHolder.state = nextState

        player.inventory.setItem(slot, itemStack)
    }

}