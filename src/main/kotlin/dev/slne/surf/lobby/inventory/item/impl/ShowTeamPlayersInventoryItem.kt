package dev.slne.surf.lobby.inventory.item.impl

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.PlayerVisibilityManager
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object ShowTeamPlayersInventoryItem : InventoryItem {
    override val slot = 7
    override val permission: String = PermissionRegistry.PLAYER_VISIBILITY_ITEM
    override val item = ItemType.ORANGE_DYE.createItemStack().apply {
        displayName {
            variableValue("Spieler-Sichtbarkeit")
        }

        buildLore {
            emptyLine()
            line {
                info("Steuert welche Spieler du sehen kannst.")
            }
            emptyLine()
            line {
                variableValue("Nur Teammitglieder werden angezeigt")
            }
        }
    }

    override fun onInteract(player: Player) {
        PlayerVisibilityManager.setState(player.uniqueId, PlayerVisibilityManager.VisibilityState.SHOW_NONE)
        player.inventory.setItem(slot, ShowNonePlayersInventoryItem.item)

        player.sendText {
            appendPrefix()
            info("Du siehst jetzt ")
            error("keine Spieler.")
        }
    }
}
