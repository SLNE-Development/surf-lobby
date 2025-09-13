package dev.slne.surf.lobby.core.lifecycle

import dev.slne.surf.cloud.api.common.util.forEachAnnotationOrdered
import dev.slne.surf.cloud.api.common.util.forEachAnnotationOrderedReversed
import dev.slne.surf.lobby.core.lifecycle.LobbyLifecycle
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component

@Component
class LobbyLifecycleHandler(private val lifecycles: ObjectProvider<LobbyLifecycle>) {

    fun onBootstrap() {
        lifecycles.forEachAnnotationOrdered { it.onBootstrap() }
    }

    suspend fun onLoad() {
        lifecycles.forEachAnnotationOrdered { it.onLoad() }
    }

    suspend fun onEnable() {
        lifecycles.forEachAnnotationOrdered { it.onEnable() }
    }

    suspend fun onDisable() {
        lifecycles.forEachAnnotationOrderedReversed { it.onDisable() }
    }
}