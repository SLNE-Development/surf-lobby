package dev.slne.surf.lobby.inventory.item.impl.pushback

import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.lore
import dev.slne.surf.lobby.core.client.item.LobbyItemContents
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendPushbackDisabled
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.PushbackManager
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object PushbackDisableInventoryItem : InventoryItem(
    LobbyItemContents.Pushback.SLOT,
    ItemType.ENDER_EYE.createItemStack().apply {
        displayName(LobbyItemContents.Pushback.name)
        lore(*LobbyItemContents.Pushback.enabledLore)
    }
) {
    override val permission: String = LobbyPermissions.PUSHBACK_ITEM

    override fun onInteract(player: Player) {
        PushbackManager.remove(player.uniqueId)
        player.inventory.setItem(slot, PushbackEnableInventoryItem.item)

        player.sendPushbackDisabled()
    }
}
