package dev.slne.surf.lobby.inventory.item.impl.visibility

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.paper.builder.buildLore
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.PlayerVisibilityManager
import dev.slne.surf.lobby.utils.PermissionRegistry
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object ShowAllPlayersInventoryItem : InventoryItem(0, ItemType.LIME_DYE.createItemStack().apply {
    displayName {
        localColored("Spieler-Sichtbarkeit")
    }

    buildLore {
        emptyLine()
        line {
            variableValue("Beschreibung:".toSmallCaps())
        }
        line {
            spacer("-")
            appendSpace()
            localColored("Spielersichtbarkeit steuern")
        }

        line {
            spacer("-")
            appendSpace()
            localColored("Zwischen allen Spielern, Teammitgliedern und keinen Spielern wechseln")
        }

        emptyLine()

        line {
            variableValue("Status".toSmallCaps())
        }

        line {
            appendSpace()
            spacer("-")
            appendSpace()
            success("Alle Spieler", TextDecoration.BOLD)
        }

        line {
            spacer("-")
            appendSpace()
            error("Nur Teammitglieder")
        }

        line {
            spacer("-")
            appendSpace()
            error("Keine Spieler")
        }

        emptyLine()

        line {
            spacer("» Klicke, um zu wechseln")
        }
    }
}) {
    override val permission: String = PermissionRegistry.PLAYER_VISIBILITY_ITEM
    override fun onInteract(player: Player) {
        PlayerVisibilityManager.setState(
            player.uniqueId,
            PlayerVisibilityManager.VisibilityState.SHOW_TEAM
        )
        player.inventory.setItem(slot, ShowTeamPlayersInventoryItem.item)

        player.sendText {
            appendInfoPrefix()
            info("Du siehst jetzt nur noch ")
            success("Teammitglieder")
            info(".")
        }
    }
}

private fun SurfComponentBuilder.localColored(text: Any, vararg decoration: TextDecoration) =
    text(text.toString(), TextColor.fromHexString("#00d0fa"), *decoration)
