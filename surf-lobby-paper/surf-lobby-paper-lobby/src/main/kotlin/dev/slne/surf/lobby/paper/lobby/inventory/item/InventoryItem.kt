package dev.slne.surf.lobby.paper.lobby.inventory.item

import com.jeff_media.morepersistentdatatypes.DataType
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

private val itemKey = NamespacedKey("surf-lobby", "item")

abstract class InventoryItem(
    val slot: Int,
    itemStack: ItemStack
) {

    val uniqueId: UUID = UUID.randomUUID()

    open val itemStack: ItemStack = itemStack.apply {
        applyUniqueId()
    }

    protected fun applyUniqueId() {
        itemStack.editPersistentDataContainer { pdc ->
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