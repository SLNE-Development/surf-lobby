package dev.slne.surf.lobby.inventory.item.impl

import dev.slne.surf.lobby.inventory.item.StatableInventoryItem
import dev.slne.surf.lobby.manager.PushbackManager
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType

object PushbackInventoryItem : StatableInventoryItem<PushbackInventoryItem.PushbackState> {
    override val slot = 0
    override val permission: String = PermissionRegistry.PUSHBACK_ITEM
    
    enum class PushbackState {
        ENABLED,
        DISABLED
    }
    
    private fun createPushbackItem(statusLine: (org.bukkit.inventory.meta.ItemMeta.() -> Unit)): ItemStack {
        return ItemType.ENDER_EYE.createItemStack().apply {
            displayName {
                variableValue("Pushback")
            }

            buildLore {
                emptyLine()
                line {
                    info("Stößt andere zurück, wenn sie zu Nahe kommen.")
                }
                emptyLine()
                statusLine()
            }
        }
    }
    
    private val enabledItem = createPushbackItem {
        line {
            success("Aktiviert")
        }
    }
    
    private val disabledItem = createPushbackItem {
        line {
            error("Deaktiviert")
        }
    }
    
    override fun getState(player: Player): PushbackState {
        return if (PushbackManager.isEnabled(player.uniqueId)) {
            PushbackState.ENABLED
        } else {
            PushbackState.DISABLED
        }
    }
    
    override fun getItemForState(state: PushbackState): ItemStack {
        return when (state) {
            PushbackState.ENABLED -> enabledItem
            PushbackState.DISABLED -> disabledItem
        }
    }
    
    override fun onInteractWithState(player: Player, currentState: PushbackState): PushbackState {
        return when (currentState) {
            PushbackState.DISABLED -> {
                PushbackManager.add(player.uniqueId)
                player.sendText {
                    appendPrefix()
                    info("Du hast den Pushback ")
                    success("aktiviert.")
                }
                PushbackState.ENABLED
            }
            PushbackState.ENABLED -> {
                PushbackManager.remove(player.uniqueId)
                player.sendText {
                    appendPrefix()
                    info("Du hast den Pushback ")
                    error("deaktiviert.")
                }
                PushbackState.DISABLED
            }
        }
    }
}
