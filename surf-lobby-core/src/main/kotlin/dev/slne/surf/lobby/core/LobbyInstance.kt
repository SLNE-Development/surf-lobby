package dev.slne.surf.lobby.core

import dev.slne.surf.lobby.LobbyApplication
import dev.slne.surf.lobby.core.lifecycle.LobbyLifecycleHandler
import org.springframework.beans.factory.getBean

object LobbyInstance {
    private val lifecycleHandler get() = LobbyApplication.context.getBean<LobbyLifecycleHandler>()

    fun onBootstrap() {
        LobbyApplication.start()
        lifecycleHandler.onBootstrap()
    }

    suspend fun onLoad() {
        lifecycleHandler.onLoad()
    }

    suspend fun onEnable() {
        lifecycleHandler.onEnable()
    }

    suspend fun onDisable() {
        lifecycleHandler.onDisable()
        LobbyApplication.stop()
    }
}