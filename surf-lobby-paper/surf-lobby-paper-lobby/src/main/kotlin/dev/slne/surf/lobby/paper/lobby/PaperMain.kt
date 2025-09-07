package dev.slne.surf.lobby.paper.lobby

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.lobby.paper.common.ContextHolder
import dev.slne.surf.lobby.paper.lobby.utils.ListenerProcessor
import org.bukkit.plugin.java.JavaPlugin
import org.springframework.beans.factory.getBean

class PaperMain : SuspendingJavaPlugin() {

    override suspend fun onEnableAsync() {
        ContextHolder.context.getBean<ListenerProcessor>().registerAll()
    }

}

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)