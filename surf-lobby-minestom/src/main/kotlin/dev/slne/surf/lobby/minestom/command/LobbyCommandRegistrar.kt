package dev.slne.surf.lobby.minestom.command

import com.google.inject.Inject
import dev.slne.minestom.lobby.api.command.CommandRegistrar

class LobbyCommandRegistrar @Inject constructor() : CommandRegistrar {
    override fun register() {
        lobbyCommand()
        spawnCommand()
    }
}
