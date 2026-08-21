package dev.slne.surf.lobby.inventory.item.impl.navigator

import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.lore
import dev.slne.surf.api.paper.inventory.framework.open
import dev.slne.surf.lobby.core.client.item.LobbyItemContents
import dev.slne.surf.lobby.inventory.impl.NavigatorInventory
import dev.slne.surf.lobby.inventory.item.InventoryItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object NavigatorItem : InventoryItem(
    LobbyItemContents.Navigator.SLOT,
    ItemType.COMPASS.createItemStack().apply {
        displayName(LobbyItemContents.Navigator.name)
        lore(*LobbyItemContents.Navigator.lore)
    }
) {
    override val permission = null
    override fun onInteract(player: Player) {
        NavigatorInventory.open(player)
    }
}
