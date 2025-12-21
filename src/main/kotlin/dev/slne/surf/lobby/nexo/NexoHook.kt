package dev.slne.surf.lobby.nexo

import com.nexomc.nexo.api.NexoItems
import org.bukkit.inventory.ItemType

object NexoHook {
    fun getInvisibleItem() =
        NexoItems.itemFromId("invisible_item")?.finalItemStack ?: ItemType.PAPER.createItemStack()
}