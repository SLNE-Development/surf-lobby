package dev.slne.surf.lobby.paper.server.commands

import dev.slne.surf.lobby.core.lifecycle.LobbyLifecycle
import org.springframework.stereotype.Component

@Component
class CommandManager : LobbyLifecycle {
    override suspend fun onEnable() {
        lobbyCommand()
    }
}