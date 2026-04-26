package dev.slne.surf.lobby.hook.nexo

import com.nexomc.nexo.api.NexoItems
import org.bukkit.inventory.ItemType

object NexoHook {
    fun getInvisibleItem() =
        NexoItems.itemFromId("invisible_item")?.build()
            ?: ItemType.RED_STAINED_GLASS_PANE.createItemStack()
}