package dev.slne.surf.lobby.minestom.visibility

import dev.slne.minestom.lobby.api.player.LobbyPlayer
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.visibility.PlayerVisibilityStates
import dev.slne.surf.lobby.core.client.visibility.PlayerVisibilityStates.VisibilityState
import net.minestom.server.entity.Entity
import java.util.*

object PlayerVisibilityService {

    fun setState(player: LobbyPlayer, state: VisibilityState) {
        PlayerVisibilityStates.setState(player.uuid, state)
        updatePlayerVisibility(player)
    }

    fun remove(uuid: UUID) {
        PlayerVisibilityStates.remove(uuid)
    }

    fun onPlayerJoin(player: LobbyPlayer) {
        updatePlayerVisibility(player)
    }

    private fun updatePlayerVisibility(player: LobbyPlayer) {
        val state = PlayerVisibilityStates.getState(player.uuid)

        player.updateViewerRule { entity -> isVisibleTo(state, entity) }
    }

    private fun isVisibleTo(state: VisibilityState, entity: Entity): Boolean {
        return entity !is LobbyPlayer || when (state) {
            VisibilityState.SHOW_ALL -> true
            VisibilityState.SHOW_TEAM -> entity.hasPermission(LobbyPermissions.PLAYER_VISIBILITY_TEAM)
            VisibilityState.SHOW_NONE -> false
        }
    }
}
