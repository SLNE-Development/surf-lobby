@file:Suppress("UnstableApiUsage")

package dev.slne.surf.lobby.paper.lobby.features.pushback

import dev.slne.surf.lobby.paper.lobby.inventory.item.items.InventoryItemMeta
import dev.slne.surf.lobby.paper.lobby.inventory.item.items.state.Statable
import dev.slne.surf.lobby.paper.lobby.inventory.item.items.state.StateClass
import dev.slne.surf.lobby.paper.lobby.inventory.item.items.state.StateInventoryItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

@InventoryItemMeta(slot = 3)
class PushbackItem(private val pushbackManager: PushbackManager) :
    StateInventoryItem<PushbackItem.PushbackItemState.Companion, PushbackItem.PushbackItemState>(
        PushbackItemState
    ) {

    override fun onStateChange(
        player: Player,
        oldState: PushbackItemState,
        newState: PushbackItemState
    ) {
        when (newState) {
            PushbackItemState.ON -> pushbackManager.add(player.uniqueId)
            PushbackItemState.OFF -> pushbackManager.remove(player.uniqueId)
        }
    }

    override fun supplyItemStack(player: Player) = ItemType.ENDER_EYE.createItemStack().apply {
        displayName {
            variableValue("Pushback")
        }

        buildLore {
            emptyLine()
            line {
                info("Stößt andere zurück, wenn sie zu Nahe kommen.")
            }
            emptyLine()
            line {
                append(stateClass.buildStateText(player.uniqueId))
            }
        }
    }


    enum class PushbackItemState : Statable {
        ON {
            override val displayName = "An"
        },
        OFF {
            override val displayName = "Aus"
        };

        companion object : StateClass<PushbackItemState>() {
            override val allStates = entries
            override val statePrefix = "Pushback"
            override val initialState = OFF
        }
    }
}