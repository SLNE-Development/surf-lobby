package dev.slne.surf.lobby.paper.common

import dev.slne.surf.lobby.core.LobbyInstance
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap

@Suppress("UnstableApiUsage")
class PaperBootstrap : PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        LobbyInstance.onBootstrap()
    }
}