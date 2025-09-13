package dev.slne.surf.lobby

import dev.slne.surf.cloud.api.common.CloudInstance
import dev.slne.surf.cloud.api.common.SurfCloudApplication
import dev.slne.surf.cloud.api.common.startSpringApplication
import org.springframework.context.ConfigurableApplicationContext

@SurfCloudApplication
class LobbyApplication {
    companion object {
        lateinit var context: ConfigurableApplicationContext

        fun start() {
            context = CloudInstance.startSpringApplication(LobbyApplication::class)
        }

        fun stop() {
            if (::context.isInitialized) {
                context.close()
            }
        }
    }
}