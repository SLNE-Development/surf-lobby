package dev.slne.surf.lobby.inventory.item.impl.rewards

import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.lore
import dev.slne.surf.lobby.core.client.hook.TrophyHook
import dev.slne.surf.lobby.core.client.item.LobbyItemContents
import dev.slne.surf.lobby.inventory.item.InventoryItem
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object TrophiesItem : InventoryItem(
    LobbyItemContents.Trophies.SLOT,
    ItemType.GOLD_INGOT.createItemStack().apply {
        displayName(LobbyItemContents.Trophies.name)
        lore(*LobbyItemContents.Trophies.lore)
    }
) {
    override val permission = null
    override fun onInteract(player: Player) {
        if (TrophyHook.available) {
            TrophyHook.openMenu(player.uniqueId)
        }
    }
}
