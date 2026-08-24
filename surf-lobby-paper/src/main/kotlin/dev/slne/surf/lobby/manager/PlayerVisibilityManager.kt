package dev.slne.surf.lobby.manager

import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.visibility.PlayerVisibilityStates
import dev.slne.surf.lobby.core.client.visibility.PlayerVisibilityStates.VisibilityState
import dev.slne.surf.lobby.plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object PlayerVisibilityManager {

    fun getState(uuid: UUID): VisibilityState {
        return PlayerVisibilityStates.getState(uuid)
    }

    fun setState(uuid: UUID, state: VisibilityState) {
        PlayerVisibilityStates.setState(uuid, state)
        updatePlayerVisibility(uuid)
    }

    fun remove(uuid: UUID) {
        PlayerVisibilityStates.remove(uuid)
    }

    fun onPlayerJoin(player: Player) {
        updatePlayerVisibility(player)

        val joiningIsTeamMember = player.hasPermission(LobbyPermissions.PLAYER_VISIBILITY_TEAM)
        val lobbyPlugin = plugin

        for (otherPlayer in Bukkit.getOnlinePlayers()) {
            if (otherPlayer == player) {
                continue
            }

            when (getState(otherPlayer.uniqueId)) {
                VisibilityState.SHOW_ALL -> otherPlayer.showPlayer(lobbyPlugin, player)

                VisibilityState.SHOW_TEAM -> if (joiningIsTeamMember) {
                    otherPlayer.showPlayer(lobbyPlugin, player)
                } else {
                    otherPlayer.hidePlayer(lobbyPlugin, player)
                }

                VisibilityState.SHOW_NONE -> otherPlayer.hidePlayer(lobbyPlugin, player)
            }
        }
    }

    private fun updatePlayerVisibility(uuid: UUID) {
        updatePlayerVisibility(Bukkit.getPlayer(uuid) ?: return)
    }

    private fun updatePlayerVisibility(player: Player) {
        val lobbyPlugin = plugin
        val onlinePlayers = Bukkit.getOnlinePlayers()

        when (getState(player.uniqueId)) {
            VisibilityState.SHOW_ALL -> for (otherPlayer in onlinePlayers) {
                if (otherPlayer != player) {
                    player.showPlayer(lobbyPlugin, otherPlayer)
                }
            }

            VisibilityState.SHOW_TEAM -> for (otherPlayer in onlinePlayers) {
                if (otherPlayer != player) {
                    if (otherPlayer.hasPermission(LobbyPermissions.PLAYER_VISIBILITY_TEAM)) {
                        player.showPlayer(lobbyPlugin, otherPlayer)
                    } else {
                        player.hidePlayer(lobbyPlugin, otherPlayer)
                    }
                }
            }

            VisibilityState.SHOW_NONE -> for (otherPlayer in onlinePlayers) {
                if (otherPlayer != player) {
                    player.hidePlayer(lobbyPlugin, otherPlayer)
                }
            }
        }
    }
}
