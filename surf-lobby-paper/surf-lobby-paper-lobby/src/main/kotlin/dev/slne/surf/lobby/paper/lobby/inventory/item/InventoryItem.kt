package dev.slne.surf.lobby.paper.lobby.inventory.item

import com.jeff_media.morepersistentdatatypes.DataType
import dev.slne.surf.cloud.api.common.util.findAnnotation
import dev.slne.surf.lobby.paper.lobby.inventory.item.items.InventoryItemMeta
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

private val itemKey = NamespacedKey("surf-lobby", "item")

abstract class InventoryItem {
    val uniqueId: UUID = UUID.randomUUID()
    val meta = this::class.findAnnotation<InventoryItemMeta>()
        ?: error("InventoryItemMeta not found on ${this::class}")

    abstract fun supplyItemStack(player: Player): ItemStack

    fun buildItemStack(player: Player): ItemStack {
        return supplyItemStack(player).apply {
            applyUniqueId()
        }
    }

    protected fun ItemStack.applyUniqueId() {
        editPersistentDataContainer { pdc ->
            pdc.set(itemKey, DataType.UUID, uniqueId)
        }
    }

    fun isInventoryItem(itemStack: ItemStack) =
        itemStack.persistentDataContainer.get(itemKey, DataType.UUID) == uniqueId

    abstract fun onClick(
        player: Player,
        action: InventoryItemAction
    )
}