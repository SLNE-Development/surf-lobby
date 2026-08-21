package dev.slne.surf.lobby.core.client.config

import dev.slne.surf.api.core.config.manager.SpongeConfigManager
import dev.slne.surf.api.core.config.surfConfigApi
import dev.slne.surf.lobby.core.client.platform.LobbyPlatform

val lobbyConfigHolder = LobbyConfigHolder()
val lobbyConfig get() = lobbyConfigHolder.lobbyConfig

class LobbyConfigHolder {
    private val configManager: SpongeConfigManager<LobbyConfig>

    init {
        surfConfigApi.createSpongeYmlConfig(
            LobbyConfig::class.java,
            LobbyPlatform.dataPath,
            "lobby.yml"
        )
        configManager = surfConfigApi.getSpongeConfigManagerForConfig(
            LobbyConfig::class.java
        )
        reload()
    }

    fun reload() {
        configManager.reloadFromFile()
    }

    val lobbyConfig get() = configManager.config
}
