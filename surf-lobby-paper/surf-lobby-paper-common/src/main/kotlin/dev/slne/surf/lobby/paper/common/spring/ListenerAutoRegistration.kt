package dev.slne.surf.lobby.paper.common.spring

import dev.slne.surf.cloud.api.common.util.mutableObjectListOf
import dev.slne.surf.lobby.core.lifecycle.LobbyLifecycle
import dev.slne.surf.surfapi.bukkit.api.event.register
import org.bukkit.event.Listener
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class ListenerAutoRegistration : BeanPostProcessor, LobbyLifecycle {
    private val watched = mutableObjectListOf<Listener>()

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean is Listener) {
            watched.add(bean)
        }

        return bean
    }

    override suspend fun onEnable() {
        for (listener in watched) {
            listener.register()
        }
    }
}