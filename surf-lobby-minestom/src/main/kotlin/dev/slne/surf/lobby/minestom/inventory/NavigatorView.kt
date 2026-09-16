package dev.slne.surf.lobby.minestom.inventory

import dev.slne.minestom.lobby.api.item.invisibleItem
import dev.slne.minestom.lobby.api.player.requireLobbyPlayer
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.minestom.inventory.framework.dsl.onItemClick
import dev.slne.surf.api.minestom.inventory.framework.dsl.openForPlayer
import dev.slne.surf.api.minestom.inventory.framework.dsl.slot
import dev.slne.surf.api.minestom.inventory.framework.dsl.withItem
import dev.slne.surf.api.minestom.inventory.framework.view.*
import dev.slne.surf.api.minestom.inventory.framework.view.container.dsl.blockRow
import dev.slne.surf.api.minestom.inventory.framework.view.pagination.pagination
import dev.slne.surf.api.minestom.inventory.framework.view.settings.PaginationViewRows
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.location.LobbyLocations
import dev.slne.surf.lobby.core.client.menu.LobbySelectorService
import dev.slne.surf.lobby.core.client.menu.NavigatorContents
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendNavigatorRules
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendNavigatorSpawnTeleported
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.queue.LobbyQueue
import dev.slne.surf.lobby.minestom.location.toPos
import me.devnatan.inventoryframework.View
import me.devnatan.inventoryframework.context.RenderContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.component.DataComponents
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.sound.SoundEvent

val navigatorView = surfView("Navigator") {
    settings {
        rows(5)
    }

    containerDefaults {
        blockRow(1)
        blockRow(2, exemptColumns = intArrayOf(3, 4, 5))
        blockRow(3)
        blockRow(4)
        blockRow(5)
    }

    onInit {
        layout(
            "         ",
            "   SEG   ",
            "         ",
            " XXXXX L ",
            "         "
        )
    }

    onFirstRender {
        layoutSlot('S').withItem(survivalServerItem).onClick { click ->
            val player = click.player.requireLobbyPlayer()

            if (player.hasPermission(LobbyPermissions.INSTANT_JOIN) && click.isLeftClick) {
                LobbyQueue.queueToSurvivalServer(player.uuid)
                player.closeInventory()
                return@onClick
            }

            player.teleportWithSound(LobbyLocations.SURVIVAL_TELEPORT.toPos())
            player.closeInventory()
        }

        layoutSlot('E').withItem(eventServerItem).onClick { click ->
            val player = click.player.requireLobbyPlayer()

            if (lobbyConfig.externalEventEnabled && lobbyConfig.externalEventReplacesDefault) {
                player.teleport(lobbyConfig.externalEventTeleportLocation.toPos()).thenRun {
                    player.playSound(true) {
                        type(SoundEvent.ENTITY_ENDERMAN_TELEPORT)
                        pitch(1.1f)
                    }
                }
                player.closeInventory()
                return@onClick
            }

            if (player.hasPermission(LobbyPermissions.INSTANT_JOIN) && click.isLeftClick) {
                LobbyQueue.queueToEventServer(player.uuid)
                player.closeInventory()
                return@onClick
            }

            player.teleportWithSound(LobbyLocations.EVENT_TELEPORT.toPos())
            player.closeInventory()
        }

        layoutSlot('L').withItem(lobbySelectorItem).onClick { click ->
            click.openForPlayer(lobbySelectorView())
        }
    }
}

object NavigatorView : View() {
    override fun onFirstRender(render: RenderContext) {

        render.layoutSlot('P').withItem(spawnItem).onClick { click ->
            val player = click.player
            player.closeInventory()
            player.teleport(lobbyConfig.spawnPoint.toPos()).thenRun {
                player.sendNavigatorSpawnTeleported()
            }
        }

        render.layoutSlot('R').withItem(rulesItem).onClick { click ->
            val player = click.player
            player.closeInventory()
            player.sendNavigatorRules()
        }

        render.layoutSlot('C').withItem(cosmeticsItem)
    }
}

private fun Player.teleportWithSound(pos: Pos) {
    teleport(pos).thenRun {
        playSound(true) {
            type(SoundEvent.ENTITY_ENDERMAN_TELEPORT)
            pitch(2.0f)
        }
    }
}

fun lobbySelectorView() = paginatedSurfView("Lobbies") {
    pagination {
        lazySource {
            NavigatorContents.lobbyServers()
        }

        elementFactory { _, builder, _, server ->
            builder.withItem(Material.RECOVERY_COMPASS) {
                displayName(NavigatorContents.lobbyServerName(server))
                lore(*NavigatorContents.lobbyServerLore(server).toTypedArray())
            }.onItemClick {
                LobbySelectorService.connect(this.player.uuid, server)
            }
        }
    }

    onClick {
        if (this.isOutsideClick) {
            this.back()
        }
    }

    layoutTarget('L')

    settings {
        paginationViewRows(PaginationViewRows.ONE)
    }

    onFirstRender {
        if (NavigatorContents.lobbyServers().isEmpty()) {
            slot(5, 2) {
                withItem(Material.BARRIER) {
                    displayName(NavigatorContents.noLobbyAvailableName)
                }
            }
        }
    }
}

private fun labeledInvisibleItem(name: Component, lore: List<Component> = emptyList()): ItemStack {
    var item = invisibleItem().with(
        DataComponents.CUSTOM_NAME,
        name.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    )

    if (lore.isNotEmpty()) {
        item = item.with(
            DataComponents.LORE,
            lore.map { it.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE) }
        )
    }

    return item
}

private val lobbySelectorItem
    get() = ItemStack.builder(Material.RECOVERY_COMPASS)
        .set(DataComponents.ITEM_NAME, NavigatorContents.lobbySelectorName)
        .build()

private val spawnItem
    get() = labeledInvisibleItem(NavigatorContents.spawnName)

private val rulesItem
    get() = labeledInvisibleItem(NavigatorContents.rulesName)

private val cosmeticsItem
    get() = labeledInvisibleItem(NavigatorContents.cosmeticsName)

private val survivalServerItem
    get() = ItemStack.builder(Material.GRASS_BLOCK)
        .set(DataComponents.ITEM_NAME, NavigatorContents.survivalServerName)
        .set(DataComponents.LORE, NavigatorContents.survivalServerLore().map {
            it.decoration(TextDecoration.ITALIC, false)
        })
        .build()

private val eventServerItem
    get() = ItemStack.builder(Material.CAKE)
        .set(DataComponents.ITEM_NAME, NavigatorContents.eventServerName)
        .set(DataComponents.LORE, NavigatorContents.eventServerLore().map {
            it.decoration(TextDecoration.ITALIC, false)
        })
        .build()
