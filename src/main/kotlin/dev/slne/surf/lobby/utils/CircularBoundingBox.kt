package dev.slne.surf.lobby.utils

import org.bukkit.Location

class CircularBoundingBox(var center: Location, val radius: Double) {
    private val radiusSquared = radius * radius

    fun isInside(location: Location): Boolean {
        if (location.world != center.world) return false
        val dx = location.x - center.x
        val dz = location.z - center.z
        return (dx * dx + dz * dz) <= radiusSquared
    }
}
