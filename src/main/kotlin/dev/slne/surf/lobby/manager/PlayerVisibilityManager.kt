package dev.slne.surf.lobby.manager

import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObjectMapOf
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object PlayerVisibilityManager {
    private val visibilityStates = mutableObjectMapOf<UUID, VisibilityState>()

    enum class VisibilityState {
        SHOW_ALL,
        SHOW_TEAM,
        SHOW_NONE
    }

    fun getState(uuid: UUID): VisibilityState {
        return visibilityStates.getOrDefault(uuid, VisibilityState.SHOW_ALL)
    }

    fun setState(uuid: UUID, state: VisibilityState) {
        visibilityStates[uuid] = state
        updatePlayerVisibility(uuid)
    }

    fun remove(uuid: UUID) {
        visibilityStates.remove(uuid)
    }

    fun onPlayerJoin(player: Player) {
        // Apply this player's visibility settings to see other players
        updatePlayerVisibility(player.uniqueId)
        
        // Update all other players' visibility to see this new player based on their individual settings
        Bukkit.getOnlinePlayers().forEach { otherPlayer ->
            if (otherPlayer != player) {
                val otherState = getState(otherPlayer.uniqueId)
                when (otherState) {
                    VisibilityState.SHOW_ALL -> {
                        otherPlayer.showPlayer(plugin, player)
                    }
                    VisibilityState.SHOW_TEAM -> {
                        if (player.hasPermission(PermissionRegistry.PLAYER_VISIBILITY_TEAM)) {
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
                // Show all players
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    if (otherPlayer != player) {
                        player.showPlayer(plugin, otherPlayer)
                    }
                }
            }
            VisibilityState.SHOW_TEAM -> {
                // Hide all players, then show only team members
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    if (otherPlayer != player) {
                        if (otherPlayer.hasPermission(PermissionRegistry.PLAYER_VISIBILITY_TEAM)) {
                            player.showPlayer(plugin, otherPlayer)
                        } else {
                            player.hidePlayer(plugin, otherPlayer)
                        }
                    }
                }
            }
            VisibilityState.SHOW_NONE -> {
                // Hide all players
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    if (otherPlayer != player) {
                        player.hidePlayer(plugin, otherPlayer)
                    }
                }
            }
        }
    }
}
