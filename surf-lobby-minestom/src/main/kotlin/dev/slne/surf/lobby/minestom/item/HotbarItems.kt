package dev.slne.surf.lobby.minestom.item

import dev.slne.minestom.lobby.api.player.LobbyPlayer
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.minestom.builder.buildItem
import dev.slne.surf.api.minestom.inventory.framework.open
import dev.slne.surf.lobby.core.client.hook.ParkourHook
import dev.slne.surf.lobby.core.client.hook.ProfileHook
import dev.slne.surf.lobby.core.client.hook.TrophyHook
import dev.slne.surf.lobby.core.client.item.LobbyItemContents
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendPushbackDisabled
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendPushbackEnabled
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendVisibilityShowAll
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendVisibilityShowNone
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendVisibilityShowTeam
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.platform.LobbyPlatform
import dev.slne.surf.lobby.core.client.pushback.PushbackStates
import dev.slne.surf.lobby.core.client.visibility.PlayerVisibilityStates
import dev.slne.surf.lobby.minestom.inventory.NavigatorView
import dev.slne.surf.lobby.minestom.visibility.PlayerVisibilityService
import net.kyori.adventure.text.format.TextColor
import net.minestom.server.component.DataComponents
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.component.TooltipDisplay
import net.minestom.server.network.player.ResolvableProfile
import net.minestom.server.sound.SoundEvent

object NavigatorHotbarItem : HotbarItem(LobbyItemContents.Navigator.SLOT, "navigator") {
    override val permission = null

    override fun buildItem() = buildItem(Material.COMPASS) {
        displayName(LobbyItemContents.Navigator.name)
        lore(*LobbyItemContents.Navigator.lore)
    }

    override fun onInteract(player: LobbyPlayer) {
        NavigatorView.open(player)
    }
}

object ParkourHotbarItem : HotbarItem(LobbyItemContents.Parkour.SLOT, "parkour") {
    override val permission = null

    override fun buildItem() = buildItem(Material.LEATHER_BOOTS) {
        builder.set(DataComponents.DYED_COLOR, TextColor.color(3, 252, 198))
        builder.set(
            DataComponents.TOOLTIP_DISPLAY,
            TooltipDisplay(false, setOf(DataComponents.DYED_COLOR, DataComponents.ATTRIBUTE_MODIFIERS))
        )

        displayName(LobbyItemContents.Parkour.name)
        lore(*LobbyItemContents.Parkour.lore)
    }

    override fun onInteract(player: LobbyPlayer) {
        LobbyPlatform.launch {
            if (ParkourHook.available) {
                ParkourHook.openParkourGui(player.uuid)
            }
        }
    }
}

object ProfileHotbarItem : HotbarItem(LobbyItemContents.Profile.SLOT, "profile") {
    override val permission = null

    override fun buildItem() = buildItem(Material.PLAYER_HEAD) {}

    override fun onInteract(player: LobbyPlayer) {
        if (ProfileHook.available) {
            ProfileHook.openMenu(player.uuid)
        }
    }

    override fun buildItemForPlayer(player: LobbyPlayer): ItemStack =
        buildItem(Material.PLAYER_HEAD) {
            player.skin?.let { skin: PlayerSkin ->
                builder.set(DataComponents.PROFILE, ResolvableProfile(skin))
            }
            displayName(LobbyItemContents.Profile.name(player.uuid))
            lore(*LobbyItemContents.Profile.lore)
        }
}

object TrophiesHotbarItem : HotbarItem(LobbyItemContents.Trophies.SLOT, "trophies") {
    override val permission = null

    override fun buildItem() = buildItem(Material.GOLD_INGOT) {
        displayName(LobbyItemContents.Trophies.name)
        lore(*LobbyItemContents.Trophies.lore)
    }

    override fun onInteract(player: LobbyPlayer) {
        if (TrophyHook.available) {
            TrophyHook.openMenu(player.uuid)
        }
    }
}

object PushbackDisableHotbarItem : HotbarItem(LobbyItemContents.Pushback.SLOT, "pushback_disable") {
    override val permission: String = LobbyPermissions.PUSHBACK_ITEM

    override fun buildItem() = buildItem(Material.ENDER_EYE) {
        displayName(LobbyItemContents.Pushback.name)
        lore(*LobbyItemContents.Pushback.enabledLore)
    }

    override fun onInteract(player: LobbyPlayer) {
        PushbackStates.remove(player.uuid)
        player.inventory.setItemStack(slot, PushbackEnableHotbarItem.item)

        player.sendPushbackDisabled()
    }
}

object PushbackEnableHotbarItem : HotbarItem(LobbyItemContents.Pushback.SLOT, "pushback_enable") {
    override val permission: String = LobbyPermissions.PUSHBACK_ITEM

    override fun buildItem() = buildItem(Material.ENDER_EYE) {
        displayName(LobbyItemContents.Pushback.name)
        lore(*LobbyItemContents.Pushback.disabledLore)
    }

    override fun onInteract(player: LobbyPlayer) {
        PushbackStates.add(player.uuid)
        player.inventory.setItemStack(slot, PushbackDisableHotbarItem.item)

        player.sendPushbackEnabled()
    }
}

object ShowAllPlayersHotbarItem : HotbarItem(LobbyItemContents.Visibility.SLOT, "visibility_all") {
    override val permission: String = LobbyPermissions.PLAYER_VISIBILITY_ITEM

    override fun buildItem() = buildItem(Material.LIME_CANDLE) {
        displayName(LobbyItemContents.Visibility.name)
        lore(*LobbyItemContents.Visibility.showAllLore)
    }

    override fun onInteract(player: LobbyPlayer) {
        PlayerVisibilityService.setState(
            player,
            PlayerVisibilityStates.VisibilityState.SHOW_TEAM
        )
        player.inventory.setItemStack(slot, ShowTeamPlayersHotbarItem.item)
        player.playSound(true) {
            type(SoundEvent.UI_BUTTON_CLICK)
        }

        player.sendVisibilityShowTeam()
    }
}

object ShowTeamPlayersHotbarItem : HotbarItem(LobbyItemContents.Visibility.SLOT, "visibility_team") {
    override val permission = null

    override fun buildItem() = buildItem(Material.YELLOW_CANDLE) {
        displayName(LobbyItemContents.Visibility.name)
        lore(*LobbyItemContents.Visibility.showTeamLore)
    }

    override fun onInteract(player: LobbyPlayer) {
        PlayerVisibilityService.setState(
            player,
            PlayerVisibilityStates.VisibilityState.SHOW_NONE
        )
        player.inventory.setItemStack(slot, ShowNonePlayersHotbarItem.item)
        player.playSound(true) {
            type(SoundEvent.UI_BUTTON_CLICK)
        }

        player.sendVisibilityShowNone()
    }
}

object ShowNonePlayersHotbarItem : HotbarItem(LobbyItemContents.Visibility.SLOT, "visibility_none") {
    override val permission: String = LobbyPermissions.PLAYER_VISIBILITY_ITEM

    override fun buildItem() = buildItem(Material.RED_CANDLE) {
        displayName(LobbyItemContents.Visibility.name)
        lore(*LobbyItemContents.Visibility.showNoneLore)
    }

    override fun onInteract(player: LobbyPlayer) {
        PlayerVisibilityService.setState(
            player,
            PlayerVisibilityStates.VisibilityState.SHOW_ALL
        )
        player.inventory.setItemStack(slot, ShowAllPlayersHotbarItem.item)
        player.playSound(true) {
            type(SoundEvent.UI_BUTTON_CLICK)
        }

        player.sendVisibilityShowAll()
    }
}
