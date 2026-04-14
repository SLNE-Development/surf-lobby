package dev.slne.surf.lobby

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import dev.slne.surf.api.paper.event.register
import dev.slne.surf.api.paper.extensions.pluginManager
import dev.slne.surf.api.paper.inventory.framework.register
import dev.slne.surf.lobby.command.lobbyCommand
import dev.slne.surf.lobby.command.spawnCommand
import dev.slne.surf.lobby.config.LobbyConfigHolder
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.hook.nexo.NexoHook
import dev.slne.surf.lobby.hook.npc.SurfNpcHook
import dev.slne.surf.lobby.inventory.impl.NavigatorInventory
import dev.slne.surf.lobby.inventory.impl.lobbySelectorView
import dev.slne.surf.lobby.listener.*
import dev.slne.surf.lobby.manager.PushbackManager
import dev.slne.surf.redis.RedisApi
import org.bukkit.inventory.ItemType
import org.bukkit.plugin.java.JavaPlugin

val plugin get() = JavaPlugin.getPlugin(PaperMain::class.java)

class PaperMain : SuspendingJavaPlugin() {
    lateinit var redisApi: RedisApi

    override fun onLoad() {
        NavigatorInventory.register()
        lobbySelectorView().register()
    }

    override fun onEnable() {
        if (checkNpcHook()) {
            SurfNpcHook.initialize()
            SurfNpcHook.startSyncTask()
        }

        DoubleJumpListener.register()
        SpawnLocationListener.register()
        PlayerConnectionListener.register()
        WorldProtectionListener.register()
        DamageListener.register()
        ItemInteractListener.register()
        InventoryInteractListener.register()
        PushbackListener.register()
        PlayerMoveListener.register()
        EntitySpawnListener.register()

        PushbackManager.startTask()

        lobbyCommand()
        spawnCommand()

        redisApi = RedisApi.create()
        eventServerBridge.init()
        redisApi.freezeAndConnect()
    }

    override fun onDisable() {
        if (checkNpcHook()) {
            SurfNpcHook.stopSyncTask()
        }

        redisApi.disconnect()
    }

    fun getInvisibleItem() =
        if (checkNexoHook()) NexoHook.getInvisibleItem() else ItemType.PAPER.createItemStack()

    fun checkTrophyHook() = pluginManager.isPluginEnabled("surf-trophy-paper")
    fun checkProfileHook() = pluginManager.isPluginEnabled("surf-profile-paper")
    fun checkParkourHook() = pluginManager.isPluginEnabled("surf-parkour-paper")
    fun checkSettingsHook() = pluginManager.isPluginEnabled("surf-settings-paper")
    fun checkNpcHook() = pluginManager.isPluginEnabled("surf-npc-paper")
    fun checkNexoHook() = pluginManager.isPluginEnabled("Nexo")
}

val lobbyConfigHolder = LobbyConfigHolder()
val lobbyConfig get() = lobbyConfigHolder.lobbyConfig