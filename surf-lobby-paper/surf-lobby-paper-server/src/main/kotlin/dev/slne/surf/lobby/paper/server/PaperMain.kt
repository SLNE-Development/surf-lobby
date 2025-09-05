package dev.slne.surf.lobby.paper.server

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.lobby.paper.server.commands.lobbyCommand
import org.bukkit.plugin.java.JavaPlugin

class PaperMain : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        lobbyCommand()
    }

}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)