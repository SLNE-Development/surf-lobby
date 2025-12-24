package dev.slne.surf.lobby.manager

import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.core.api.util.mutableObject2ObjectMapOf
import dev.slne.surf.tab.api.redis.TabHideRedisEvent
import dev.slne.surf.tab.api.redis.TabShowRedisEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object PlayerVisibilityManager {
    private val visibilityStates = mutableObject2ObjectMapOf<UUID, VisibilityState>()

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

    /**
     * Forces a player to show another player with entity refresh.
     * Hides the target first to ensure the client properly respawns the entity.
     */
    private fun forceShowPlayer(observer: Player, target: Player) {
        observer.hidePlayer(plugin, target)
        observer.showPlayer(plugin, target)
    }

    fun onPlayerJoin(player: Player) {
        updatePlayerVisibility(player.uniqueId)
        Bukkit.getOnlinePlayers().forEach { otherPlayer ->
            if (otherPlayer != player) {
                val otherState = getState(otherPlayer.uniqueId)
                when (otherState) {
                    VisibilityState.SHOW_ALL -> {
                        forceShowPlayer(otherPlayer, player)

                        plugin.redisApi.publishEvent(
                            TabShowRedisEvent(
                                otherPlayer.uniqueId, player.uniqueId
                            )
                        )
                    }

                    VisibilityState.SHOW_TEAM -> {
                        if (player.hasPermission(PermissionRegistry.PLAYER_VISIBILITY_TEAM)) {
                            forceShowPlayer(otherPlayer, player)

                            plugin.redisApi.publishEvent(
                                TabShowRedisEvent(
                                    otherPlayer.uniqueId, player.uniqueId
                                )
                            )
                        } else {
                            otherPlayer.hidePlayer(plugin, player)

                            plugin.redisApi.publishEvent(
                                TabHideRedisEvent(
                                    otherPlayer.uniqueId, player.uniqueId
                                )
                            )
                        }
                    }

                    VisibilityState.SHOW_NONE -> {
                        otherPlayer.hidePlayer(plugin, player)

                        plugin.redisApi.publishEvent(
                            TabHideRedisEvent(
                                otherPlayer.uniqueId, player.uniqueId
                            )
                        )
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
                        forceShowPlayer(player, otherPlayer)

                        plugin.redisApi.publishEvent(
                            TabShowRedisEvent(
                                player.uniqueId, otherPlayer.uniqueId
                            )
                        )
                    }
                }
            }

            VisibilityState.SHOW_TEAM -> {
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    if (otherPlayer != player) {
                        if (otherPlayer.hasPermission(PermissionRegistry.PLAYER_VISIBILITY_TEAM)) {
                            forceShowPlayer(player, otherPlayer)

                            plugin.redisApi.publishEvent(
                                TabShowRedisEvent(
                                    player.uniqueId, otherPlayer.uniqueId
                                )
                            )
                        } else {
                            player.hidePlayer(plugin, otherPlayer)

                            plugin.redisApi.publishEvent(
                                TabHideRedisEvent(
                                    player.uniqueId, otherPlayer.uniqueId
                                )
                            )
                        }
                    }
                }
            }

            VisibilityState.SHOW_NONE -> {
                Bukkit.getOnlinePlayers().forEach { otherPlayer ->
                    if (otherPlayer != player) {
                        player.hidePlayer(plugin, otherPlayer)

                        plugin.redisApi.publishEvent(
                            TabHideRedisEvent(
                                player.uniqueId, otherPlayer.uniqueId
                            )
                        )
                    }
                }
            }
        }
    }
}
