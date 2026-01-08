package dev.slne.surf.lobby.inventory.item.impl.navigator

import dev.slne.surf.lobby.inventory.impl.navigatorInventory
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object NavigatorItem : InventoryItem(4, ItemType.COMPASS.createItemStack().apply {
    displayName {
        localColored("Navigator")
    }

    buildLore {
        emptyLine()
        line {
            variableValue("Beschreibung:".toSmallCaps())
        }
        line {
            spacer("-")
            appendSpace()
            localColored("Teleport zum Event-Schiff")
        }

        line {
            spacer("-")
            appendSpace()
            localColored("Teleport zum Survival-Schiff")
        }
        emptyLine()

        line {
            spacer("» Klicke, um den Navigator zu öffnen")
        }
    }
}) {
    override val permission = null
    override fun onInteract(player: Player) {
        navigatorInventory().show(player)
    }
}

private fun SurfComponentBuilder.localColored(text: Any, vararg decoration: TextDecoration) =
    text(text.toString(), TextColor.fromHexString("#f58442"), *decoration)