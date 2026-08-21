@file:Suppress("UnstableApiUsage")

package dev.slne.surf.lobby.inventory.impl

import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.paper.builder.buildItem
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.builder.lore
import dev.slne.surf.api.paper.inventory.framework.dsl.onItemClick
import dev.slne.surf.api.paper.inventory.framework.dsl.openForPlayer
import dev.slne.surf.api.paper.inventory.framework.dsl.slot
import dev.slne.surf.api.paper.inventory.framework.view.layoutTarget
import dev.slne.surf.api.paper.inventory.framework.view.onFirstRender
import dev.slne.surf.api.paper.inventory.framework.view.paginatedSurfView
import dev.slne.surf.api.paper.inventory.framework.view.pagination.pagination
import dev.slne.surf.api.paper.inventory.framework.view.settings
import dev.slne.surf.api.paper.inventory.framework.view.settings.PaginationViewRows
import dev.slne.surf.lobby.config.toLocation
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.location.LobbyLocations
import dev.slne.surf.lobby.core.client.menu.LobbySelectorService
import dev.slne.surf.lobby.core.client.menu.NavigatorContents
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendNavigatorRules
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendNavigatorSpawnTeleported
import dev.slne.surf.lobby.core.client.permission.LobbyPermissions
import dev.slne.surf.lobby.core.client.queue.LobbyQueue
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.getLocation
import me.devnatan.inventoryframework.View
import me.devnatan.inventoryframework.ViewConfigBuilder
import me.devnatan.inventoryframework.context.RenderContext
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemType

object NavigatorInventory : View() {
    override fun onInit(config: ViewConfigBuilder) {
        config.size(6).cancelInteractions().layout(
            "ASSSAEEEA",
            "ASSSAEEEA",
            "ASSSAEEEA",
            "AAAAAAAAA",
            "LLPPARRCC",
            "LLPPARRCC"
        ).title(buildText {
            text("<shift:-48><glyph:server_selector>")
        })
    }

    override fun onFirstRender(render: RenderContext) {
        render.layoutSlot('S').withItem(survivalServerItem).onClick { click ->
            val player = click.player

            if (player.hasPermission(LobbyPermissions.INSTANT_JOIN) && click.isLeftClick) {
                LobbyQueue.queueToSurvivalServer(player.uniqueId)
                player.closeInventory()
                return@onClick
            }

            player.teleportAsync(LobbyLocations.SURVIVAL_TELEPORT.getLocation()).thenRun {
                player.playSound(true) {
                    type(Sound.ENTITY_ENDERMAN_TELEPORT)
                    pitch(2.0f)
                }
            }
            player.closeInventory()
        }

        render.layoutSlot('E').withItem(eventServerItem).onClick { click ->
            val player = click.player

            if (lobbyConfig.externalEventEnabled && lobbyConfig.externalEventReplacesDefault) {
                player.teleportAsync(lobbyConfig.externalEventTeleportLocation.toLocation())
                    .thenRun {
                        player.playSound(true) {
                            type(Sound.ENTITY_ENDERMAN_TELEPORT)
                            pitch(1.1f)
                        }
                    }
                player.closeInventory()
                return@onClick
            }

            if (player.hasPermission(LobbyPermissions.INSTANT_JOIN) && click.isLeftClick) {
                LobbyQueue.queueToEventServer(player.uniqueId)
                player.closeInventory()
                return@onClick
            }

            player.teleportAsync(LobbyLocations.EVENT_TELEPORT.getLocation()).thenRun {
                player.playSound(true) {
                    type(Sound.ENTITY_ENDERMAN_TELEPORT)
                    pitch(2.0f)
                }
            }
            player.closeInventory()
        }

        render.layoutSlot('L').withItem(lobbySelectorItem).onClick { click ->
            click.openForPlayer(lobbySelectorView())
        }

        render.layoutSlot('P').withItem(spawnItem).onClick { click ->
            val player = click.player
            player.closeInventory()
            player.teleportAsync(lobbyConfig.spawnPoint.toLocation()).thenRun {
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

fun lobbySelectorView() = paginatedSurfView("Lobbies") {
    pagination {
        lazySource {
            NavigatorContents.lobbyServers()
        }

        elementFactory { _, builder, _, server ->
            builder.withItem(buildItem(ItemType.RECOVERY_COMPASS) {
                displayName(NavigatorContents.lobbyServerName(server))
                lore(*NavigatorContents.lobbyServerLore(server).toTypedArray())
            }).onItemClick {
                LobbySelectorService.connect(this.player.uniqueId, server)
            }
        }
    }

    layoutTarget('L')

    settings {
        paginationViewRows(PaginationViewRows.ONE)
    }

    onFirstRender {
        if (NavigatorContents.lobbyServers().isEmpty()) {
            slot(5, 2) {
                withItem(buildItem(Material.BARRIER) {
                    displayName(NavigatorContents.noLobbyAvailableName)
                })
            }
        }
    }
}

private val lobbySelectorItem
    get() = plugin.getInvisibleItem().apply {
        displayName(NavigatorContents.lobbySelectorName)
    }

private val spawnItem
    get() = plugin.getInvisibleItem().apply {
        displayName(NavigatorContents.spawnName)
    }

private val rulesItem
    get() = plugin.getInvisibleItem().apply {
        displayName(NavigatorContents.rulesName)
    }

private val cosmeticsItem
    get() = plugin.getInvisibleItem().apply {
        displayName(NavigatorContents.cosmeticsName)
    }

private val survivalServerItem
    get() = plugin.getInvisibleItem().apply {
        displayName(NavigatorContents.survivalServerName)
        lore(*NavigatorContents.survivalServerLore().toTypedArray())
    }

private val eventServerItem
    get() = plugin.getInvisibleItem().apply {
        displayName(NavigatorContents.eventServerName)
        lore(*NavigatorContents.eventServerLore().toTypedArray())
    }
