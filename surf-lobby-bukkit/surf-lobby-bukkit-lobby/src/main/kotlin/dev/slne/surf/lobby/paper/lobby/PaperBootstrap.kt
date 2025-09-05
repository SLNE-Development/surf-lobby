package dev.slne.surf.lobby.paper.lobby

import dev.slne.surf.cloud.api.common.CloudInstance
import dev.slne.surf.cloud.api.common.startSpringApplication
import dev.slne.surf.lobby.LobbyApplication
import dev.slne.surf.lobby.paper.common.ContextHolder
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap

@Suppress("UnstableApiUsage")
class PaperBootstrap : PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        ContextHolder.context = CloudInstance.startSpringApplication(LobbyApplication::class)
    }
}