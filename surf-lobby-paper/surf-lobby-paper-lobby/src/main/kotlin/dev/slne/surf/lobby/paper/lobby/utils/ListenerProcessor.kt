package dev.slne.surf.lobby.paper.lobby.utils

import dev.slne.surf.surfapi.bukkit.api.event.register
import org.bukkit.event.Listener
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

@Component
class ListenerProcessor : BeanPostProcessor {

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean !is Listener) return bean

        bean.register()
        
        return bean
    }
}