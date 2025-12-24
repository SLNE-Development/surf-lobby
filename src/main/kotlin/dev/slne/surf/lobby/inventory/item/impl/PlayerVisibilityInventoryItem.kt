package dev.slne.surf.lobby.inventory.item.impl

import dev.slne.surf.lobby.inventory.item.StatableInventoryItem
import dev.slne.surf.lobby.manager.PlayerVisibilityManager
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType

object PlayerVisibilityInventoryItem : StatableInventoryItem<PlayerVisibilityManager.VisibilityState> {
    override val slot = 2
    override val permission: String = PermissionRegistry.PLAYER_VISIBILITY_ITEM
    
    private fun createVisibilityItem(
        itemType: ItemType,
        statusLine: (org.bukkit.inventory.meta.ItemMeta.() -> Unit)
    ): ItemStack {
        return itemType.createItemStack().apply {
            displayName {
                variableValue("Spieler-Sichtbarkeit")
            }

            buildLore {
                emptyLine()
                line {
                    info("Steuert welche Spieler du sehen kannst.")
                }
                emptyLine()
                statusLine()
            }
        }
    }
    
    private val showAllItem = createVisibilityItem(ItemType.LIME_DYE) {
        line {
            success("Alle Spieler werden angezeigt")
        }
    }
    
    private val showTeamItem = createVisibilityItem(ItemType.ORANGE_DYE) {
        line {
            variableValue("Nur Teammitglieder werden angezeigt")
        }
    }
    
    private val showNoneItem = createVisibilityItem(ItemType.GRAY_DYE) {
        line {
            error("Keine Spieler werden angezeigt")
        }
    }
    
    override fun getState(player: Player): PlayerVisibilityManager.VisibilityState {
        return PlayerVisibilityManager.getState(player.uniqueId)
    }
    
    override fun getItemForState(state: PlayerVisibilityManager.VisibilityState): ItemStack {
        return when (state) {
            PlayerVisibilityManager.VisibilityState.SHOW_ALL -> showAllItem
            PlayerVisibilityManager.VisibilityState.SHOW_TEAM -> showTeamItem
            PlayerVisibilityManager.VisibilityState.SHOW_NONE -> showNoneItem
        }
    }
    
    override fun onInteractWithState(
        player: Player,
        currentState: PlayerVisibilityManager.VisibilityState
    ): PlayerVisibilityManager.VisibilityState {
        val newState = when (currentState) {
            PlayerVisibilityManager.VisibilityState.SHOW_ALL -> {
                player.sendText {
                    appendPrefix()
                    info("Du siehst jetzt nur noch ")
                    success("Teammitglieder.")
                }
                PlayerVisibilityManager.VisibilityState.SHOW_TEAM
            }
            PlayerVisibilityManager.VisibilityState.SHOW_TEAM -> {
                player.sendText {
                    appendPrefix()
                    info("Du siehst jetzt ")
                    error("keine Spieler.")
                }
                PlayerVisibilityManager.VisibilityState.SHOW_NONE
            }
            PlayerVisibilityManager.VisibilityState.SHOW_NONE -> {
                player.sendText {
                    appendPrefix()
                    info("Du siehst jetzt ")
                    success("alle Spieler.")
                }
                PlayerVisibilityManager.VisibilityState.SHOW_ALL
            }
        }
        
        PlayerVisibilityManager.setState(player.uniqueId, newState)
        return newState
    }
}
