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

    SURVIVAL_TELEPORT(
        world = Bukkit.getWorlds().first(),
        x = 128.0,
        y = 148.0,
        z = 415.0,
        yaw = 0.0f,
        pitch = 0.0f
    ),

    EVENT_TELEPORT(
        world = Bukkit.getWorlds().first(),
        x = 110.0,
        y = 146.0,
        z = 214.0,
        yaw = -180.0f,
        pitch = 0.0f
    );


    fun getLocation() =
        Location(world, x, y, z, yaw, pitch)
}