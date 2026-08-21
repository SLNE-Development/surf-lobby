package dev.slne.surf.lobby.minestom

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import dev.slne.minestom.lobby.api.instance.LobbyInstance
import dev.slne.minestom.lobby.api.plugin.MinestomPluginEntrypoint
import dev.slne.minestom.lobby.api.plugin.annotation.DataDirectory
import dev.slne.surf.api.minestom.inventory.framework.register
import dev.slne.surf.lobby.core.client.config.lobbyConfigHolder
import dev.slne.surf.lobby.core.client.event.eventServerBridge
import dev.slne.surf.lobby.core.client.redis.lobbyRedisLoader
import dev.slne.surf.lobby.minestom.inventory.NavigatorView
import dev.slne.surf.lobby.minestom.inventory.lobbySelectorView
import dev.slne.surf.lobby.minestom.npc.LobbyNpcs
import dev.slne.surf.lobby.minestom.pushback.PushbackTask
import net.minestom.server.instance.InstanceContainer
import java.nio.file.Path

@Singleton
class LobbyMinestomEntrypoint @Inject constructor(
    @DataDirectory path: Path,
    @LobbyInstance lobbyInstance: Provider<InstanceContainer>
) : MinestomPluginEntrypoint {

    init {
        dataPath = path
        lobbyInstanceProvider = lobbyInstance
    }

    override suspend fun start() {
        lobbyConfigHolder.reload()

        lobbyRedisLoader.onLoad()
        eventServerBridge.init()
        lobbyRedisLoader.onEnable()

        NavigatorView.register()
        lobbySelectorView().register()

        LobbyNpcs.spawnAll()
        LobbyNpcs.startSyncTask()
        PushbackTask.start()
    }

    override suspend fun stop() {
        LobbyNpcs.stopSyncTask()
        PushbackTask.stop()
        lobbyRedisLoader.disconnect()
    }

    companion object {
        lateinit var dataPath: Path
            private set

        private lateinit var lobbyInstanceProvider: Provider<InstanceContainer>

        /**
         * The lobby world.
         *
         * The server creates it while it starts up, so this is only readable afterwards - the
         * plugin's entrypoint is built before that happens.
         */
        val lobbyInstance: InstanceContainer get() = lobbyInstanceProvider.get()
    }
}
