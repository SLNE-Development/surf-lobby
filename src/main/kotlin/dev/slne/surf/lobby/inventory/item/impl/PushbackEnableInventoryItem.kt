package dev.slne.surf.lobby.inventory.item.impl

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.PushbackManager
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object PushbackEnableInventoryItem : InventoryItem {
    override val slot = 3
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
                error("Deaktiviert")
            }
        }
    }

    override fun onInteract(player: Player) {
        PushbackManager.add(player.uniqueId)
        player.inventory.setItem(slot, PushbackDisableInventoryItem.item)
    }
}