package dev.slne.surf.lobby.inventory.item.impl.parkour

import com.github.shynixn.mccoroutine.folia.entityDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.lore
import dev.slne.surf.lobby.core.client.hook.ParkourHook
import dev.slne.surf.lobby.core.client.item.LobbyItemContents
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.plugin
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.DyedItemColor
import io.papermc.paper.datacomponent.item.TooltipDisplay
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType

@Suppress("UnstableApiUsage")
object ParkourItem : InventoryItem(
    LobbyItemContents.Parkour.SLOT,
    ItemType.LEATHER_BOOTS.createItemStack().apply {
        setData(
            DataComponentTypes.DYED_COLOR,
            DyedItemColor.dyedItemColor(Color.fromRGB(3, 252, 198))
        )
        setData(
            DataComponentTypes.TOOLTIP_DISPLAY,
            TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(
                    DataComponentTypes.DYED_COLOR,
                    DataComponentTypes.ATTRIBUTE_MODIFIERS
                )
        )

        displayName(LobbyItemContents.Parkour.name)
        lore(*LobbyItemContents.Parkour.lore)
    }
) {
    override val permission = null
    override fun onInteract(player: Player) {
        plugin.launch(plugin.entityDispatcher(player)) {
            if (ParkourHook.available) {
                ParkourHook.openParkourGui(player.uniqueId)
            }
        }
    }
}
