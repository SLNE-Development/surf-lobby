package dev.slne.surf.lobby.core.lifecycle

interface LobbyLifecycle {
    fun onBootstrap() = Unit
    suspend fun onLoad() = Unit
    suspend fun onEnable() = Unit
    suspend fun onDisable() = Unit
}