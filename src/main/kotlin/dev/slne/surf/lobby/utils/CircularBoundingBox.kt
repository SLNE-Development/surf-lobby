package dev.slne.surf.lobby.utils

class CircularBoundingBox(
    var centerX: Double,
    var centerZ: Double,
    val radius: Double
) {
    private val radiusSquared = radius * radius

    fun isInside(x: Double, z: Double): Boolean {
        val dx = x - centerX
        val dz = z - centerZ
        return dx * dx + dz * dz <= radiusSquared
    }

    fun updateCenter(x: Double, z: Double) {
        centerX = x
        centerZ = z
    }
}
