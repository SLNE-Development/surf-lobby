package dev.slne.surf.lobby.inventory.item.impl.pushback

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.PushbackManager
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object PushbackEnableInventoryItem : InventoryItem(2, ItemType.ENDER_EYE.createItemStack().apply {
    displayName {
        variableValue("Pushback")
    }

    buildLore {
        emptyLine()
        line {
            variableValue("Beschreibung:".toSmallCaps())
        }
        line {
            spacer("-")
            appendSpace()
            localColored("Pushback steuern")
        }

        line {
            spacer("-")
            appendSpace()
            localColored("Pushback aktivieren oder deaktivieren")
        }

        emptyLine()

        line {
            variableValue("Status".toSmallCaps())
        }

        line {
            spacer("-")
            appendSpace()
            error("Aktiviert")
        }

        line {
            appendSpace()
            spacer("-")
            appendSpace()
            success("Deaktiviert", TextDecoration.BOLD)
        }

        emptyLine()

        line {
            spacer("» Klicke, um zu wechseln")
        }
    }
}) {
    override val permission: String = PermissionRegistry.PUSHBACK_ITEM
    override fun onInteract(player: Player) {
        PushbackManager.add(player.uniqueId)
        player.inventory.setItem(slot, PushbackDisableInventoryItem.item)

        player.sendText {
            appendPrefix()
            info("Du hast den Pushback ")
            success("aktiviert.")
        }
    }
}

private fun SurfComponentBuilder.localColored(text: Any, vararg decoration: TextDecoration) =
    text(text.toString(), TextColor.fromHexString("#f5426c"), *decoration)