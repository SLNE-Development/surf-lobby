package dev.slne.surf.lobby.minestom.util

import net.minestom.server.ServerFlag
import net.minestom.server.coordinate.Vec

/**
 * Interprets this vector as a velocity in blocks per tick and converts it to the blocks per
 * second Minestom expects.
 */
fun Vec.blocksPerTick(): Vec = mul(ServerFlag.SERVER_TICKS_PER_SECOND.toDouble())
