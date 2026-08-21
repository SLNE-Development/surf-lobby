package dev.slne.surf.lobby.utils

import dev.slne.surf.lobby.core.client.location.LobbyLocations
import org.bukkit.Bukkit
import org.bukkit.Location

fun LobbyLocations.getLocation() =
    Location(Bukkit.getWorlds().first(), x, y, z, yaw, pitch)
