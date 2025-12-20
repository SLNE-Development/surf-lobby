package dev.slne.surf.lobby.utils

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {

    private const val PREFIX = "surf.lobby"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_LOBBY = create("$COMMAND_PREFIX.lobby")
    val COMMAND_LOBBY_RELOAD = create("$COMMAND_PREFIX.lobby.reload")

    val PUSHBACK_ATTACK = create("$PREFIX.pushback.attack")
    val PUSHBACK_ITEM = create("$PREFIX.pushback.item")
    val PROTECTION_BYPASS = create("$PREFIX.bypass")
}