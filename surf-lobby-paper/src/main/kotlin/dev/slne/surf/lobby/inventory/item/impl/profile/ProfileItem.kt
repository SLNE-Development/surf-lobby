package dev.slne.surf.lobby.inventory.item.impl.profile

import dev.slne.surf.api.paper.builder.buildItem
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.lore
import dev.slne.surf.lobby.core.client.hook.ProfileHook
import dev.slne.surf.lobby.core.client.item.LobbyItemContents
import dev.slne.surf.lobby.inventory.item.InventoryItem
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ResolvableProfile.resolvableProfile
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType

object ProfileItem : InventoryItem(
    LobbyItemContents.Profile.SLOT,
    ItemType.PLAYER_HEAD.createItemStack()
) {
    override val permission = null
    override fun onInteract(player: Player) {
        if (ProfileHook.available) {
            ProfileHook.openMenu(player.uniqueId)
        }
    }

    @Suppress("UnstableApiUsage")
    override fun getItemForPlayer(player: Player): ItemStack = buildItem(ItemType.PLAYER_HEAD) {
        setData(DataComponentTypes.PROFILE, resolvableProfile(player.playerProfile))
        displayName(LobbyItemContents.Profile.name(player.uniqueId))
        lore(*LobbyItemContents.Profile.lore)
    }
}
