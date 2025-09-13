package dev.slne.surf.lobby.paper.lobby.inventory.item.items.state

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterAccess
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import it.unimi.dsi.fastutil.objects.ObjectList
import java.util.*
import kotlin.enums.EnumEntries
import kotlin.time.Duration.Companion.hours

interface StateHolder<S> where S : Enum<S>, S : Statable {
    val initialState: S
    var state: S

    fun getNextState(): S
    fun getPreviousState(): S

    fun getAllStates(): ObjectList<S>
}


abstract class StateClass<S> where S : Enum<S>, S : Statable {
    private val states = Caffeine.newBuilder()
        .expireAfterAccess(1.hours)
        .maximumSize(1000)
        .softValues()
        .build<UUID, S> { initialState }

    abstract val initialState: S
    abstract val allStates: EnumEntries<S>
    abstract val statePrefix: String

    fun getState(uuid: UUID): S = states.get(uuid)
    fun nextState(uuid: UUID): Pair<S, S> {
        val current = getState(uuid)
        val next = allStates[(current.ordinal + 1) % allStates.size]
        states.put(uuid, next)
        return current to next
    }

    fun buildStateText(uuid: UUID) = buildText {
        val state = getState(uuid)
        variableKey(statePrefix)
        appendSpace()
        variableValue(state.displayName)
    }
}