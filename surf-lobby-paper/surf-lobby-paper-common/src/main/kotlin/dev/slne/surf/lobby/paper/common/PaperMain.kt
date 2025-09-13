package dev.slne.surf.lobby.paper.common

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.lobby.core.LobbyInstance
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {

    override suspend fun onLoadAsync() {
        LobbyInstance.onLoad()
    }

    override suspend fun onEnableAsync() {
        LobbyInstance.onEnable()
    }

    override suspend fun onDisableAsync() {
        LobbyInstance.onDisable()
    }
}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)