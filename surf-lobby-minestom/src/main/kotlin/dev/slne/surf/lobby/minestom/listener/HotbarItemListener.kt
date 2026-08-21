package dev.slne.surf.lobby.minestom.listener

import com.google.inject.Inject
import dev.slne.minestom.lobby.api.event.EventRegistrar
import dev.slne.minestom.lobby.api.extension.addListener
import dev.slne.minestom.lobby.api.player.LobbyPlayer
import dev.slne.minestom.lobby.api.player.lobbyPlayer
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.lobby.core.client.hook.SettingsHook
import dev.slne.surf.lobby.minestom.elytra.ElytraBoostHandler
import dev.slne.surf.lobby.minestom.item.HotbarItem
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.PlayerHand
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.inventory.InventoryPreClickEvent
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent
import net.minestom.server.event.player.PlayerSwapItemEvent
import net.minestom.server.event.player.PlayerUseItemEvent
import net.minestom.server.inventory.PlayerInventory
import net.minestom.server.item.ItemStack
import net.minestom.server.sound.SoundEvent

class HotbarItemListener @Inject constructor() : EventRegistrar {
    override fun register(node: EventNode<Event>) {
        node.addListener<PlayerUseItemEvent> { event ->
            if (event.hand != PlayerHand.MAIN) return@addListener

            if (interact(event.lobbyPlayer, event.itemStack)) {
                event.isCancelled = true
            }
        }

        node.addListener<PlayerBlockInteractEvent> { event ->
            if (event.hand != PlayerHand.MAIN) return@addListener

            val item = event.player.getItemInHand(PlayerHand.MAIN)
            if (interact(event.lobbyPlayer, item)) {
                event.isCancelled = true
            }
        }

        node.addListener<InventoryPreClickEvent> { event ->
            if (event.player.gameMode == GameMode.CREATIVE) return@addListener

            if (event.inventory is PlayerInventory) {
                event.isCancelled = true
            }
        }

        node.addListener<ItemDropEvent> { event ->
            if (event.player.gameMode == GameMode.CREATIVE) return@addListener

            event.isCancelled = true
        }

        node.addListener<PlayerSwapItemEvent> { event ->
            if (event.player.gameMode == GameMode.CREATIVE) return@addListener

            ElytraBoostHandler.checkAndBoost(event.lobbyPlayer)

            event.isCancelled = true
        }

        node.addListener<PlayerChangeHeldSlotEvent> { event ->
            if (!SettingsHook.available) return@addListener

            if (!SettingsHook.hasScrollSoundsEnabled(event.player.uuid)) return@addListener

            val player = event.lobbyPlayer

            if (HotbarItem.items.values.filter { item ->
                    item.permission?.let { player.hasPermission(it) } ?: true
                }.any { it.slot == event.newSlot.toInt() }
            ) {
                player.playSound(true) {
                    type(SoundEvent.UI_BUTTON_CLICK)
                }
            }
        }
    }

    private fun interact(player: LobbyPlayer, item: ItemStack): Boolean {
        val hotbarItem = HotbarItem.byItem(item) ?: return false
        hotbarItem.onInteract(player)
        return true
    }
}
