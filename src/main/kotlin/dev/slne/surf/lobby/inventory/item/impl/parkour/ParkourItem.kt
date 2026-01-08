package dev.slne.surf.lobby.inventory.item.impl.parkour

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType
import org.bukkit.inventory.meta.ColorableArmorMeta

object ParkourItem : InventoryItem(6, ItemType.BARRIER.createItemStack().apply {
    editMeta(ColorableArmorMeta::class.java) {
        it.setColor(Color.fromRGB(3, 252, 198))
    }

    displayName {
        localColored("Error")
    }

    buildLore {
        emptyLine()
        line {
            variableValue("Du solltest dieses Item nicht sehen. Wenn du dieses Item siehst, melde dies bitte einem Teammitglied.".toSmallCaps())
        }
    }
}) {
    override val permission = null
    override fun onInteract(player: Player) {}
}


private fun SurfComponentBuilder.localColored(text: Any, vararg decoration: TextDecoration) =
    text(text.toString(), TextColor.fromHexString("#03fcc6"), *decoration)