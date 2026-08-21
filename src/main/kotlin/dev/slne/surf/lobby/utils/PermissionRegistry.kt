package dev.slne.surf.lobby.utils

import dev.slne.surf.api.paper.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {

    private const val PREFIX = "surf.lobby"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_SPAWN = create("$COMMAND_PREFIX.spawn")
    val COMMAND_LOBBY = create("$COMMAND_PREFIX.lobby")
    val COMMAND_LOBBY_RELOAD = create("$COMMAND_PREFIX.lobby.reload")

    val EVENT_BYPASS = create("$PREFIX.event.bypass")
    val QUEUE_BYPASS = create("$PREFIX.queue.bypass")
    val SURVIVAL_BYPASS = create("$PREFIX.survival.bypass")

    val PUSHBACK_ATTACK = create("$PREFIX.pushback.attack")
    val PUSHBACK_ITEM = create("$PREFIX.pushback.item")

    val PLAYER_VISIBILITY_ITEM = create("$PREFIX.playervisibility.item")
    val PLAYER_VISIBILITY_TEAM = create("$PREFIX.playervisibility.team")
    val ELYTRA_BOOST = create("$PREFIX.elytraboost")

    val INSTANT_JOIN = create("$PREFIX.instantjoin")
}