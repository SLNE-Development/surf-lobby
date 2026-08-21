package dev.slne.surf.lobby.utils

import dev.slne.surf.api.paper.permission.PermissionRegistry
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions

object PermissionRegistry : PermissionRegistry() {

    val COMMAND_SPAWN = create(LobbyPermissions.COMMAND_SPAWN)
    val COMMAND_LOBBY = create(LobbyPermissions.COMMAND_LOBBY)
    val COMMAND_LOBBY_RELOAD = create(LobbyPermissions.COMMAND_LOBBY_RELOAD)

    val EVENT_BYPASS = create(LobbyPermissions.EVENT_BYPASS)
    val QUEUE_BYPASS = create(LobbyPermissions.QUEUE_BYPASS)
    val SURVIVAL_BYPASS = create(LobbyPermissions.SURVIVAL_BYPASS)

    val PUSHBACK_ATTACK = create(LobbyPermissions.PUSHBACK_ATTACK)
    val PUSHBACK_ITEM = create(LobbyPermissions.PUSHBACK_ITEM)

    val PLAYER_VISIBILITY_ITEM = create(LobbyPermissions.PLAYER_VISIBILITY_ITEM)
    val PLAYER_VISIBILITY_TEAM = create(LobbyPermissions.PLAYER_VISIBILITY_TEAM)
    val ELYTRA_BOOST = create(LobbyPermissions.ELYTRA_BOOST)

    val INSTANT_JOIN = create(LobbyPermissions.INSTANT_JOIN)
}
