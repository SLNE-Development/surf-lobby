package dev.slne.surf.lobby

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.lobby.command.lobbyCommand
import dev.slne.surf.lobby.command.spawnCommand
import dev.slne.surf.lobby.config.LobbyConfigHolder
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.event.listener.EventServerStateChangeListener
import dev.slne.surf.lobby.hologram.SurfHologramHook
import dev.slne.surf.lobby.listener.*
import dev.slne.surf.lobby.manager.PushbackManager
import dev.slne.surf.lobby.nexo.NexoHook
import dev.slne.surf.lobby.npc.SurfNpcHook
import dev.slne.surf.redis.RedisApi
import dev.slne.surf.surfapi.bukkit.api.event.register
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemType
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)

class PaperMain : SuspendingJavaPlugin() {
    lateinit var redisApi: RedisApi
    override fun onEnable() {
        if (surfNpcHook) {
            SurfNpcHook.initialize()
        }

        if (surfHologramHook) {
            SurfHologramHook.initialize()
        }

        DoubleJumpListener.register()
        SpawnLocationListener.register()
        PlayerConnectionListener.register()
        WorldProtectionListener.register()
        DamageListener.register()
        ItemInteractListener.register()
        InventoryInteractListener.register()
        PushbackListener.register()

        PushbackManager.startTask()

        lobbyCommand()
        spawnCommand()

        redisApi = RedisApi.create(plugin.dataPath)
        redisApi.registerRequestHandler(EventServerStateChangeListener)
        redisApi.freezeAndConnect()

        launch {
            eventServerBridge.requestState()
        }
    }

    override fun onDisable() {
        redisApi.disconnect()
    }

    fun getInvisibleItem() =
        if (nexoHook) NexoHook.getInvisibleItem() else ItemType.PAPER.createItemStack()
}

val lobbyConfigHolder = LobbyConfigHolder()
val lobbyConfig get() = lobbyConfigHolder.lobbyConfig
val surfNpcHook get() = Bukkit.getPluginManager().isPluginEnabled("surf-npc-bukkit")
val surfHologramHook get() = Bukkit.getPluginManager().isPluginEnabled("surf-hologram-paper")
val nexoHook get() = Bukkit.getPluginManager().isPluginEnabled("Nexo")