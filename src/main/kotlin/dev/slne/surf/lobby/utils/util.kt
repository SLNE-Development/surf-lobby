package dev.slne.surf.lobby.utils

import dev.slne.surf.hologram.api.hologramConversationUtil
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location

fun Location.toHologramLocation() = hologramConversationUtil.createLocation(
    hologramConversationUtil.createWorld(
        world.name,
        world.uid
    ),
    x,
    y,
    z,
    yaw,
    pitch
)

fun SurfComponentBuilder.note(message: Any) =
    append { text(message.toString(), TextColor.fromHexString("#6EA6D9")!!) }