package dev.slne.surf.lobby.paper.lobby.inventory.item.items

import org.springframework.stereotype.Component

@Component
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InventoryItemMeta(val slot: Int)
