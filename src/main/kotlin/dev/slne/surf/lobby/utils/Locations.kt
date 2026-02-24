package dev.slne.surf.lobby.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

enum class Locations(
    val world: World,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    SURVIVAL_NPC(
        world = Bukkit.getWorlds().first(),
        x = 137.5,
        y = 149.0,
        z = 423.5,
        yaw = 90.0f,
        pitch = 0.0f
    ),

    EVENT_NPC(
        world = Bukkit.getWorlds().first(),
        x = 110.5,
        y = 147.0,
        z = 200.5,
        yaw = 0.0f,
        pitch = 0.0f
    ),

    SPAWN_SURVIVAL(
        world = Bukkit.getWorlds().first(),
        x = 95.5,
        y = 149.0,
        z = 319.5,
        yaw = -120.0f,
        pitch = 0.0f
    ),
    SPAWN_EVENT(
        world = Bukkit.getWorlds().first(),
        x = 95.5,
        y = 149.0,
        z = 311.5,
        yaw = -60.0f,
        pitch = 0.0f
    ),
    SPAWN_RULES(
        world = Bukkit.getWorlds().first(),
        x = 97.5,
        y = 149.0,
        z = 323.5,
        yaw = -143.0f,
        pitch = 0.0f
    ),
    SPAWN_SHOP(
        world = Bukkit.getWorlds().first(),
        x = 97.5,
        y = 149.0,
        z = 307.5,
        yaw = -37.0f,
        pitch = 0.0f
    ),

    SURVIVAL_TELEPORT(
        world = Bukkit.getWorlds().first(),
        x = 122.5,
        y = 148.0,
        z = 423.5,
        yaw = -90.0f,
        pitch = 0.0f
    ),

    EVENT_TELEPORT(
        world = Bukkit.getWorlds().first(),
        x = 110.5,
        y = 146.0,
        z = 214.5,
        yaw = -180.0f,
        pitch = 0.0f
    );


    fun getLocation() =
        Location(world, x, y, z, yaw, pitch)
}