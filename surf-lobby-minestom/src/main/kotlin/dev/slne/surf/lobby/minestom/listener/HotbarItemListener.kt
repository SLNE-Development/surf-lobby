package dev.slne.surf.lobby.minestom.listener

import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.minestom.event.EventRegistrar
import dev.slne.surf.api.minestom.extension.addListener
import dev.slne.surf.api.minestom.permission.hasPermission
import dev.slne.surf.lobby.core.client.hook.SettingsHook
import dev.slne.surf.lobby.minestom.elytra.ElytraBoostHandler
import dev.slne.surf.lobby.minestom.item.HotbarItem
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
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

object HotbarItemListener : EventRegistrar {
    override fun register(node: EventNode<Event>) {
        node.addListener<PlayerUseItemEvent> { event ->
            if (event.hand != PlayerHand.MAIN) return@addListener

            if (interact(event.player, event.itemStack)) {
                event.isCancelled = true
            }
        }

        node.addListener<PlayerBlockInteractEvent> { event ->
            if (event.hand != PlayerHand.MAIN) return@addListener

            val item = event.player.getItemInHand(PlayerHand.MAIN)
            if (interact(event.player, item)) {
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

            ElytraBoostHandler.checkAndBoost(event.player)

            event.isCancelled = true
        }

        node.addListener<PlayerChangeHeldSlotEvent> { event ->
            if (!SettingsHook.available) return@addListener

            if (!SettingsHook.hasScrollSoundsEnabled(event.player.uuid)) return@addListener

            val player = event.player

            val item = HotbarItem.bySlot[event.newSlot.toInt()] ?: return@addListener
            val permission = item.permission

            if (permission != null && !player.hasPermission(permission)) {
                return@addListener
            }

            player.playSound(true) {
                type(SoundEvent.UI_BUTTON_CLICK)
            }
        }
    }

    private fun interact(player: Player, item: ItemStack): Boolean {
        val hotbarItem = HotbarItem.byItem(item) ?: return false
        hotbarItem.onInteract(player)
        return true
    }
}
