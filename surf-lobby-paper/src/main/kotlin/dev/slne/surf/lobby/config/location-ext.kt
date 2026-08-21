package dev.slne.surf.lobby.config

import dev.slne.surf.lobby.core.client.config.LobbyConfig
import org.bukkit.Bukkit
import org.bukkit.Location

fun LobbyConfig.LocationConfig.toLocation(): Location {
    val worldInstance = Bukkit.getWorld(world)
        ?: throw IllegalArgumentException("World '$world' not found")

    return Location(worldInstance, x, y, z, yaw, pitch)
}
