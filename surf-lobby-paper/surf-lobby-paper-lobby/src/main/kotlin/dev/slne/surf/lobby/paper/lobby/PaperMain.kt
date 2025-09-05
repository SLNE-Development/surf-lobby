package dev.slne.surf.lobby.paper.lobby

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
    }

}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)