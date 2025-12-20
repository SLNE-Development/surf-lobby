package dev.slne.surf.lobby

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.lobby.config.LobbyConfig
import dev.slne.surf.lobby.hologram.SurfHologramHook
import dev.slne.surf.lobby.listener.DoubleJumpListener
import dev.slne.surf.lobby.listener.GameModeListener
import dev.slne.surf.lobby.listener.SpawnLocationListener
import dev.slne.surf.lobby.listener.WorldProtectionListener
import dev.slne.surf.lobby.npc.SurfNpcHook
import dev.slne.surf.surfapi.bukkit.api.event.register
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)

class PaperMain : SuspendingJavaPlugin() {
    override fun onEnable() {
        if (surfNpcHook) {
            SurfNpcHook.initialize()
        }

        if (surfHologramHook) {
            SurfHologramHook.initialize()
        }

        DoubleJumpListener.register()
        SpawnLocationListener.register()
        GameModeListener.register()
        WorldProtectionListener.register()
    }
}

val lobbyConfig get() = LobbyConfig.getConfig()

val surfNpcHook get() = Bukkit.getPluginManager().isPluginEnabled("surf-npc-bukkit")
val surfHologramHook get() = Bukkit.getPluginManager().isPluginEnabled("surf-hologram-paper")