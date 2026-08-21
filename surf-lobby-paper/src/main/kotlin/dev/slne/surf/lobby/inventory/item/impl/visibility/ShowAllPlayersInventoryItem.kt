package dev.slne.surf.lobby.inventory.item.impl.visibility

import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.lore
import dev.slne.surf.api.paper.util.BukkitSound
import dev.slne.surf.lobby.core.client.item.LobbyItemContents
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendVisibilityShowTeam
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.visibility.PlayerVisibilityStates
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.PlayerVisibilityManager
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

object ShowAllPlayersInventoryItem : InventoryItem(
    LobbyItemContents.Visibility.SLOT,
    ItemType.LIME_CANDLE.createItemStack().apply {
        displayName(LobbyItemContents.Visibility.name)
        lore(*LobbyItemContents.Visibility.showAllLore)
    }
) {
    override val permission: String = LobbyPermissions.PLAYER_VISIBILITY_ITEM
    override fun onInteract(player: Player) {
        PlayerVisibilityManager.setState(
            player.uniqueId,
            PlayerVisibilityStates.VisibilityState.SHOW_TEAM
        )
        player.inventory.setItem(slot, ShowTeamPlayersInventoryItem.item)
        player.playSound(true) {
            type(BukkitSound.UI_BUTTON_CLICK)
        }

        player.sendVisibilityShowTeam()
    }
}
