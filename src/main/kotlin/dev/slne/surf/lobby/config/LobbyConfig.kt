package dev.slne.surf.lobby.config

import org.bukkit.Location
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class LobbyConfig(
    val minHeight: Int = 0,
    val eventServerName: String = "event",
    val survivalServerName: String = "survival",
    val lobbyCategory: String = "lobby",
    val spawnPoint: LocationConfig = LocationConfig.default(),
) {
    @ConfigSerializable
    data class LocationConfig(
        val world: String,
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float,
        val pitch: Float
    ) {
        fun toLocation(): Location {
            val worldInstance = org.bukkit.Bukkit.getWorld(world)
                ?: throw IllegalArgumentException("World '$world' not found")

            return Location(worldInstance, x, y, z, yaw, pitch)
        }

        companion object {
            fun default() = LocationConfig(
                world = "world",
                x = 0.5,
                y = 100.0,
                z = 0.5,
                yaw = 0.0f,
                pitch = 0.0f
            )
        }
    }
}