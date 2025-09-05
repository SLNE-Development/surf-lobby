package dev.slne.surf.lobby.paper.lobby.features.pushback

import dev.slne.surf.lobby.paper.common.ContextHolder
import dev.slne.surf.lobby.paper.lobby.inventory.item.items.state.Statable
import dev.slne.surf.lobby.paper.lobby.inventory.item.items.state.StateHolder
import dev.slne.surf.lobby.paper.lobby.inventory.item.items.state.StateInventoryItem
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.util.toObjectList
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.springframework.beans.factory.getBean

class PushbackItem(
    slot: Int,
    itemStack: ItemStack,
    initialState: PushbackItemState = PushbackItemState.OFF,
) : StateInventoryItem<PushbackItem.PushbackItemStateHolder, PushbackItem.PushbackItemState>(
    slot,
    itemStack,
    PushbackItemStateHolder(
        initialState
    )
) {
    private val pushbackManager by lazy {
        ContextHolder.context.getBean<PushbackManager>()
    }

    override fun onStateChange(
        player: Player,
        oldState: PushbackItemState,
        newState: PushbackItemState
    ) {
        when (newState) {
            PushbackItemState.ON -> pushbackManager.pushback.add(player.uniqueId)
            PushbackItemState.OFF -> pushbackManager.pushback.remove(player.uniqueId)
        }
    }

    class PushbackItemStateHolder(
        override val initialState: PushbackItemState
    ) : StateHolder<PushbackItemState> {
        override var state: PushbackItemState = initialState

        override fun getNextState(): PushbackItemState {
            return when (state) {
                PushbackItemState.ON -> PushbackItemState.OFF
                PushbackItemState.OFF -> PushbackItemState.ON
            }
        }

        override fun getPreviousState(): PushbackItemState {
            return when (state) {
                PushbackItemState.ON -> PushbackItemState.OFF
                PushbackItemState.OFF -> PushbackItemState.ON
            }
        }

        override fun getAllStates() = PushbackItemState.entries.toObjectList()
    }

    enum class PushbackItemState : Statable {
        ON {
            override fun asComponent() = buildText {
                primary("Pushback: ")
                success("An")
            }
        },
        OFF {
            override fun asComponent() = buildText {
                primary("Pushback: ")
                error("Aus")
            }
        };
    }

}