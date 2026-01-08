package dev.slne.surf.lobby.inventory.item.impl.rewards

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object TrophiesItem : InventoryItem(2, ItemType.GOLD_INGOT.createItemStack().apply {
    displayName {
        localColored("Trophäen")
    }

    buildLore {
        emptyLine()
        line {
            variableValue("Beschreibung:".toSmallCaps())
        }
        line {
            spacer("-")
            appendSpace()
            localColored("Siehe deine Erfolge an")
        }

        line {
            spacer("-")
            appendSpace()
            localColored("Erkunde alle Trophäen")
        }
        emptyLine()

        line {
            spacer("» Klicke, um deine Erfolge zu öffnen")
        }
    }
}) {
    override val permission = null
    override fun onInteract(player: Player) {}
}

private fun SurfComponentBuilder.localColored(text: Any, vararg decoration: TextDecoration) =
    text(text.toString(), TextColor.fromHexString("#ffe700"), *decoration)