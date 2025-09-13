package dev.slne.surf.lobby.paper.lobby.inventory.item.items.state

import dev.slne.surf.lobby.paper.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.paper.lobby.inventory.item.InventoryItemAction
import org.bukkit.entity.Player

abstract class StateInventoryItem<C : StateClass<S>, S>(
    val stateClass: C,
) : InventoryItem() where S : Enum<S>, S : Statable {

    abstract fun onStateChange(player: Player, oldState: S, newState: S)

    override fun onClick(player: Player, action: InventoryItemAction) {
        val (old, new) = stateClass.nextState(player.uniqueId)
        onStateChange(player, old, new)
        player.inventory.setItem(meta.slot, buildItemStack(player))
    }
}