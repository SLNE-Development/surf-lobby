package dev.slne.surf.lobby.core.client.menu

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterWrite
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.server.SurfServer
import dev.slne.surf.core.api.common.server.state.SurfServerState
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendAlreadyConnecting
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendServerUnreachable
import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import java.util.*
import kotlin.time.Duration.Companion.minutes

/**
 * Connects players to the lobby server they picked in the selector, making sure a player only
 * has one connection attempt running at a time.
 */
object LobbySelectorService {

    private val connectingPlayers = Caffeine.newBuilder()
        .expireAfterWrite(5.minutes)
        .build<UUID, Long>()

    fun connect(playerUuid: UUID, server: SurfServer) {
        val audience = LobbyPlatform.audience(playerUuid)

        if (connectingPlayers.getIfPresent(playerUuid) != null) {
            audience?.sendAlreadyConnecting()
            return
        }

        val updated = SurfCoreApi.getServerByName(server.name) ?: return

        if (updated.state != SurfServerState.RUNNING) {
            audience?.sendServerUnreachable()
            return
        }

        connectingPlayers.put(playerUuid, System.currentTimeMillis())

        LobbyPlatform.launch {
            try {
                val surfPlayer = SurfCoreApi.getPlayer(playerUuid)
                    ?: error("SurfPlayer for player $playerUuid not found!")
                val result = SurfCoreApi.sendPlayerAwaiting(surfPlayer, updated)

                if (result.velocityMessage != null) {
                    audience?.sendText {
                        appendErrorPrefix()
                        result.velocityMessage?.let {
                            append(it)
                        }
                    }
                } else {
                    if (!result.isSuccessful()) {
                        audience?.sendText {
                            appendErrorPrefix()
                            error("Du konntest nicht mit dem Server verbunden werden: ${result.status}")
                        }
                    }
                }
            } finally {
                connectingPlayers.invalidate(playerUuid)
            }
        }
    }
}
