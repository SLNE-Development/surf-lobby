package dev.slne.surf.lobby.minestom.visibility

import dev.slne.surf.api.minestom.permission.hasPermission
import dev.slne.surf.api.minestom.visibility.PlayerVisibility
import dev.slne.surf.friends.api.player.FriendsPlayer
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.visibility.PlayerVisibilityStates
import dev.slne.surf.lobby.core.client.visibility.PlayerVisibilityStates.VisibilityState
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import java.util.*

object PlayerVisibilityService {
    fun setState(player: Player, state: VisibilityState) {
        PlayerVisibilityStates.setState(player.uuid, state)
        updatePlayerVisibility(player)
    }

    fun remove(uuid: UUID) {
        PlayerVisibilityStates.remove(uuid)
    }

    fun onPlayerJoin(player: Player) {
        updatePlayerVisibility(player)
    }

    private fun updatePlayerVisibility(player: Player) {
        PlayerVisibility.updateViewerRule(player) { entity -> isVisibleTo(player, entity) }
    }

    private fun isVisibleTo(player: Player, entity: Entity): Boolean {
        val state = PlayerVisibilityStates.getState(player.uuid)
        val friendPlayer = FriendsPlayer[player.uuid]

        return entity !is Player || when (state) {
            VisibilityState.SHOW_ALL -> true
            VisibilityState.SHOW_TEAM -> entity.hasPermission(LobbyPermissions.PLAYER_VISIBILITY_TEAM)
            VisibilityState.SHOW_NONE -> false
            VisibilityState.SHOW_FRIENDS -> friendPlayer.onlineFriendUuids.contains(entity.uuid)
        }
    }
}
