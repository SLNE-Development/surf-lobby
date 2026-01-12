package dev.slne.surf.lobby.inventory.item.impl.parkour

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.parkour.ParkourHook
import dev.slne.surf.lobby.plugin
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

object ParkourItem : InventoryItem(1, ItemType.LEATHER_BOOTS.createItemStack().apply {
    editMeta(ColorableArmorMeta::class.java) {
        it.setColor(Color.fromRGB(3, 252, 198))
    }

    displayName {
        localColored("Parkour")
    }

    buildLore {
        emptyLine()
        line {
            variableValue("Beschreibung:".toSmallCaps())
        }
        line {
            spacer("-")
            appendSpace()
            localColored("Starte den Lobby Parkour")
        }

        line {
            spacer("-")
            appendSpace()
            localColored("Siehe Statistiken an")
        }

        line {
            spacer("-")
            appendSpace()
            localColored("Stelle neue Rekorde auf")
        }
        emptyLine()

        line {
            spacer("» Klicke, um das Parkour Menu zu öffnen")
        }
    }
}) {
    override val permission = null
    override fun onInteract(player: Player) {
        plugin.launch {
            ParkourHook.openParkourGui(player)
        }
    }
}


private fun SurfComponentBuilder.localColored(text: Any, vararg decoration: TextDecoration) =
    text(text.toString(), TextColor.fromHexString("#03fcc6"), *decoration)