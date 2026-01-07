package dev.slne.surf.lobby.inventory.item.impl.pushback

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.PushbackManager
import dev.slne.surf.lobby.utils.PermissionRegistry
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object PushbackDisableInventoryItem : InventoryItem {
    override val slot = 0
    override val item = ItemType.ENDER_EYE.createItemStack().apply {
        displayName {
            variableValue("Pushback")
        }

        buildLore {
            emptyLine()
            line {
                info("Stößt andere zurück, wenn sie zu Nahe kommen.")
            }
            emptyLine()
            line {
                success("Aktiviert")
            }
        }
    }
    override val permission: String = PermissionRegistry.PUSHBACK_ITEM

    override fun onInteract(player: Player) {
        PushbackManager.remove(player.uniqueId)
        player.inventory.setItem(slot, PushbackEnableInventoryItem.item)

        player.sendText {
            appendPrefix()
            info("Du hast den Pushback ")
            error("deaktiviert.")
        }
    }
}