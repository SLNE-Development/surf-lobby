package dev.slne.surf.lobby.minestom

import dev.slne.surf.api.minestom.coroutine.minestomAsyncScope
import dev.slne.surf.api.minestom.extension.InstanceManager
import dev.slne.surf.api.minestom.inventory.framework.register
import dev.slne.surf.api.minestom.plugin.SurfMinestomPlugin
import dev.slne.surf.lobby.core.client.config.lobbyConfigHolder
import dev.slne.surf.lobby.core.client.event.EventServerBridge
import dev.slne.surf.lobby.core.client.redis.lobbyRedisLoader
import dev.slne.surf.lobby.minestom.command.lobbyCommand
import dev.slne.surf.lobby.minestom.command.spawnCommand
import dev.slne.surf.lobby.minestom.inventory.NavigatorView
import dev.slne.surf.lobby.minestom.inventory.lobbySelectorView
import dev.slne.surf.lobby.minestom.listener.DoubleJumpListener
import dev.slne.surf.lobby.minestom.listener.HotbarItemListener
import dev.slne.surf.lobby.minestom.listener.LobbyPlayerListener
import dev.slne.surf.lobby.minestom.listener.MovementListener
import dev.slne.surf.lobby.minestom.listener.PushbackAttackListener
import dev.slne.surf.lobby.minestom.listener.WorldProtectionListener
import dev.slne.surf.lobby.minestom.npc.LobbyNpcs
import dev.slne.surf.lobby.minestom.pushback.PushbackTask
import net.minestom.server.instance.Instance

object LobbyMinestomPlugin : SurfMinestomPlugin() {

    /**
     * The lobby world.
     *
     * The server creates it before it loads the plugins and registers no other instance.
     */
    val lobbyInstance: Instance
        get() = checkNotNull(InstanceManager.instances.firstOrNull()) {
            "The lobby world has not been created yet"
        }

    override suspend fun onLoad() {
        lobbyConfigHolder.reload()

        lobbyRedisLoader.onLoad()
        EventServerBridge.init(minestomAsyncScope)
        lobbyRedisLoader.onEnable()

        // The view frame initializes its views when the server enables, before the plugins are
        // enabled, so views registered later than this could not be opened.
        NavigatorView.register()
        lobbySelectorView().register()
    }

    override suspend fun onEnable() {
        LobbyPlayerListener.register(eventNode)
        DoubleJumpListener.register(eventNode)
        MovementListener.register(eventNode)
        HotbarItemListener.register(eventNode)
        WorldProtectionListener.register(eventNode)
        PushbackAttackListener.register(eventNode)

        lobbyCommand()
        spawnCommand()

        LobbyNpcs.spawnAll()
        LobbyNpcs.startSyncTask()
        PushbackTask.start()
    }

    override suspend fun onDisable() {
        LobbyNpcs.stopSyncTask()
        PushbackTask.stop()
        lobbyRedisLoader.disconnect()
    }
}
