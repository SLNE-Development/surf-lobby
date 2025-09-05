package dev.slne.surf.lobby.paper.lobby.inventory.item.items.state

import it.unimi.dsi.fastutil.objects.ObjectList

interface StateHolder<S : Statable> {
    val initialState: S
    var state: S

    fun getNextState(): S
    fun getPreviousState(): S

    fun getAllStates(): ObjectList<S>
}