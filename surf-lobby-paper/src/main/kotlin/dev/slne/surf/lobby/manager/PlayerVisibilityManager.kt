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
        updatePlayerVisibility(player.uniqueId)
        Bukkit.getOnlinePlayers().forEach { otherPlayer ->
            if (otherPlayer != player) {
                val otherState = getState(otherPlayer.uniqueId)
                when (otherState) {
                    VisibilityState.SHOW_ALL -> {
                        otherPlayer.showPlayer(plugin, player)
                    }

                    VisibilityState.SHOW_TEAM -> {
                        if (player.hasPermission(LobbyPermissions.PLAYER_VISIBILITY_TEAM)) {
                            otherPlayer.showPlayer(plugin, player)
                        } else {
                            otherPlayer.hidePlayer(plugin, player)
                        }
                    }

                    VisibilityState.SHOW_NONE -> {
                        otherPlayer.hidePlayer(plugin, player)
                    }
                }
            }
        }
    }

    private fun updatePlayerVisibility(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid) ?: return
        val state = getState(uuid)

        when (state) {
            VisibilityState.SHOW_ALL -> {
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    if (otherPlayer != player) {
                        player.showPlayer(plugin, otherPlayer)
                    }
                }
            }

            VisibilityState.SHOW_TEAM -> {
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    if (otherPlayer != player) {
                        if (otherPlayer.hasPermission(LobbyPermissions.PLAYER_VISIBILITY_TEAM)) {
                            player.showPlayer(plugin, otherPlayer)
                        } else {
                            player.hidePlayer(plugin, otherPlayer)
                        }
                    }
                }
            }

            VisibilityState.SHOW_NONE -> {
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    if (otherPlayer != player) {
                        player.hidePlayer(plugin, otherPlayer)
                    }
                }
            }
        }
    }
}
