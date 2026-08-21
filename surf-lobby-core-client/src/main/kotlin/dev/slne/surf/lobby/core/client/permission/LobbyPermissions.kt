package dev.slne.surf.lobby.core.client.permission

/**
 * The permission strings the lobby uses on every platform.
 */
object LobbyPermissions {

    private const val PREFIX = "surf.lobby"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    const val COMMAND_SPAWN = "$COMMAND_PREFIX.spawn"
    const val COMMAND_LOBBY = "$COMMAND_PREFIX.lobby"
    const val COMMAND_LOBBY_RELOAD = "$COMMAND_PREFIX.lobby.reload"

    const val EVENT_BYPASS = "$PREFIX.event.bypass"
    const val QUEUE_BYPASS = "$PREFIX.queue.bypass"
    const val SURVIVAL_BYPASS = "$PREFIX.survival.bypass"

    const val PUSHBACK_ATTACK = "$PREFIX.pushback.attack"
    const val PUSHBACK_ITEM = "$PREFIX.pushback.item"

    const val PLAYER_VISIBILITY_ITEM = "$PREFIX.playervisibility.item"
    const val PLAYER_VISIBILITY_TEAM = "$PREFIX.playervisibility.team"
    const val ELYTRA_BOOST = "$PREFIX.elytraboost"

    const val INSTANT_JOIN = "$PREFIX.instantjoin"
}
