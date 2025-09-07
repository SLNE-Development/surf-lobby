package dev.slne.surf.lobby.paper.lobby.utils

import dev.slne.surf.cloud.api.common.util.mutableObjectSetOf
import dev.slne.surf.surfapi.bukkit.api.event.register
import org.bukkit.event.Listener
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class ListenerProcessor : BeanPostProcessor {

    private val watched = mutableObjectSetOf<Listener>()

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean is Listener) {
            watched.add(bean)
        }

        return bean
    }

    fun registerAll() = watched.forEach { it.register() }
}