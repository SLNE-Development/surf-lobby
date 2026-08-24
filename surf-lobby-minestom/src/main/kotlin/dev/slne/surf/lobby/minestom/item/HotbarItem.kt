package dev.slne.surf.lobby.minestom.item

import dev.slne.minestom.lobby.api.player.LobbyPlayer
import dev.slne.surf.api.core.util.freeze
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minestom.server.item.ItemStack
import net.minestom.server.tag.Tag

abstract class HotbarItem(
    val slot: Int,
    private val id: String
) {
    abstract val permission: String?
    abstract fun onInteract(player: LobbyPlayer)

    protected abstract fun buildItem(): ItemStack

    val item: ItemStack by lazy { buildItem().withTag(TAG, id) }

    /**
     * Whether a joining player starts with this item in [slot].
     */
    open val placedOnJoin: Boolean get() = true

    /**
     * Builds the item for a specific player. Override this method to provide player-specific
     * items. By default, the static item is used.
     */
    protected open fun buildItemForPlayer(player: LobbyPlayer): ItemStack? = null

    fun getItemForPlayer(player: LobbyPlayer): ItemStack =
        buildItemForPlayer(player)?.withTag(TAG, id) ?: item

    companion object {
        private val TAG = Tag.String("surf_lobby:hotbar_item")

        private val all = listOf(
            PushbackDisableHotbarItem,
            PushbackEnableHotbarItem,
            NavigatorHotbarItem,
            ShowNonePlayersHotbarItem,
            ShowTeamPlayersHotbarItem,
            ShowAllPlayersHotbarItem,
            ProfileHotbarItem,
            ParkourHotbarItem,
            TrophiesHotbarItem
        )

        private val byId = all.associateByTo(Object2ObjectOpenHashMap()) { it.id }.freeze()

        /**
         * The item each slot starts with, keyed by slot.
         */
        val bySlot: Int2ObjectMap<HotbarItem> = Int2ObjectOpenHashMap<HotbarItem>(all.size).apply {
            for (item in all) {
                if (!item.placedOnJoin) {
                    continue
                }

                val clash = put(item.slot, item)

                require(clash == null) {
                    "Slot ${item.slot} is placed on join by both " +
                            "${clash?.javaClass?.simpleName} and ${item.javaClass.simpleName}"
                }
            }
        }.freeze()

        fun byItem(stack: ItemStack): HotbarItem? {
            val id = stack.getTag(TAG) ?: return null
            return byId[id]
        }
    }
}
