package dev.slne.surf.lobby.paper.common.utils

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object PermissionRegistry : PermissionRegistry() {

    private const val PREFIX = "surf.lobby"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val LOBBY = create("$COMMAND_PREFIX.lobby")

    val PUSHBACK_ATTACK = create("$PREFIX.pushback.attack")
    val PUSHBACK_ITEM = create("$PREFIX.pushback.item")

}