package dev.slne.surf.lobby.minestom.location

import dev.slne.surf.lobby.core.client.config.LobbyConfig
import dev.slne.surf.lobby.core.client.location.LobbyLocations
import net.minestom.server.coordinate.Pos

fun LobbyLocations.toPos() = Pos(x, y, z, yaw, pitch)

fun LobbyConfig.LocationConfig.toPos() = Pos(x, y, z, yaw, pitch)
