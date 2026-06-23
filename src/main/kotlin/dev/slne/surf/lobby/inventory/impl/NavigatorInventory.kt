@file:Suppress("UnstableApiUsage")

package dev.slne.surf.lobby.inventory.impl

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.shynixn.mccoroutine.folia.launch
import com.sksamuel.aedile.core.expireAfterWrite
import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.clickOpensUrl
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.builder.buildItem
import dev.slne.surf.api.paper.builder.buildLore
import dev.slne.surf.api.paper.builder.displayName
import dev.slne.surf.api.paper.inventory.framework.dsl.onItemClick
import dev.slne.surf.api.paper.inventory.framework.dsl.openForPlayer
import dev.slne.surf.api.paper.inventory.framework.dsl.slot
import dev.slne.surf.api.paper.inventory.framework.view.layoutTarget
import dev.slne.surf.api.paper.inventory.framework.view.onFirstRender
import dev.slne.surf.api.paper.inventory.framework.view.paginatedSurfView
import dev.slne.surf.api.paper.inventory.framework.view.pagination.pagination
import dev.slne.surf.api.paper.inventory.framework.view.settings
import dev.slne.surf.api.paper.inventory.framework.view.settings.PaginationViewRows
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.server.state.SurfServerState
import dev.slne.surf.core.api.paper.util.surfPlayer
import dev.slne.surf.event.base.api.common.state.EventServerState
import dev.slne.surf.lobby.event.eventServerBridge
import dev.slne.surf.lobby.lobbyConfig
import dev.slne.surf.lobby.plugin
import dev.slne.surf.lobby.utils.Locations
import me.devnatan.inventoryframework.View
import me.devnatan.inventoryframework.ViewConfigBuilder
import me.devnatan.inventoryframework.context.RenderContext
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemType
import java.util.*
import kotlin.time.Duration.Companion.minutes

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
            player.teleportAsync(Locations.SURVIVAL_TELEPORT.getLocation()).thenRun {
                player.playSound(true) {
                    type(Sound.ENTITY_ENDERMAN_TELEPORT)
                    pitch(2.0f)
                }
            }
            player.closeInventory()
        }

        render.layoutSlot('E').withItem(eventServerItem).onClick { click ->
            val player = click.player

            if(lobbyConfig.externalEventEnabled && lobbyConfig.externalEventReplacesDefault) {
                player.teleportAsync(lobbyConfig.externalEventTeleportLocation.toLocation()).thenRun {
                   player.playSound(true) {
                       type(Sound.ENTITY_ENDERMAN_TELEPORT)
                       pitch(2.0f)
                   }
                }
                player.closeInventory()
                return@onClick
            }
            
            player.teleportAsync(Locations.EVENT_TELEPORT.getLocation()).thenRun {
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
                player.sendText {
                    appendInfoPrefix()
                    info("Du wurdest zum Spawn teleportiert.")
                }
            }
        }

        render.layoutSlot('R').withItem(rulesItem).onClick { click ->
            val player = click.player
            player.closeInventory()
            player.sendText {
                appendInfoPrefix()
                info("Das Regelwerk findest du hier: ")
                append {
                    variableValue("server.castcrafter.de/rules")
                    clickOpensUrl("https://server.castcrafter.de/rules")
                }
            }
        }

        render.layoutSlot('C').withItem(cosmeticsItem)
    }
}

private val connectingPlayers =
    Caffeine.newBuilder().expireAfterWrite(5.minutes).build<UUID, Long>()

fun lobbySelectorView() = paginatedSurfView("Lobbies") {
    pagination {
        lazySource {
            SurfCoreApi.getServerByCategory(lobbyConfig.lobbyCategory)
                .sortedBy { it.displayName }
        }

        elementFactory { _, builder, _, server ->
            builder.withItem(buildItem(ItemType.RECOVERY_COMPASS) {
                displayName { variableValue(server.displayName) }

                buildLore {
                    emptyLine()
                    line {
                        spacer("-")
                        appendSpace()
                        note("Status: ")
                        variableValue(
                            if (server.state == SurfServerState.RUNNING) "Online" else "Offline"
                        )
                    }
                    line {
                        spacer("-")
                        appendSpace()
                        note("Spieler: ")
                        variableValue("${server.getPlayerCount()} / ${server.maxPlayers}")
                    }

                    if (server.state == SurfServerState.RUNNING) {
                        emptyLine()
                        line {
                            spacer("» Klicke zum Verbinden")
                        }
                    }
                }
            }).onItemClick {
                val player = this.player
                val uuid = player.uniqueId

                if (connectingPlayers.getIfPresent(uuid) != null) {
                    player.sendText {
                        appendInfoPrefix()
                        error("Du verbindest dich bereits mit einem Server...")
                    }
                    return@onItemClick
                }

                val updated = SurfCoreApi.getServerByName(server.name) ?: return@onItemClick

                if (updated.state != SurfServerState.RUNNING) {
                    player.sendText {
                        appendInfoPrefix()
                        error("Dieser Server ist derzeit nicht erreichbar!")
                    }
                    return@onItemClick
                }

                connectingPlayers.put(uuid, System.currentTimeMillis())

                plugin.launch {
                    try {
                        val result = SurfCoreApi.sendPlayerAwaiting(player.surfPlayer, updated)

                        if (result.velocityMessage != null) {
                            player.sendText {
                                appendErrorPrefix()
                                result.velocityMessage?.let {
                                    append(it)
                                }
                            }
                        } else {
                            if (!result.isSuccessful()) {
                                player.sendText {
                                    appendErrorPrefix()
                                    error("Du konntest nicht mit dem Server verbunden werden: ${result.status}")
                                }
                            }
                        }
                    } finally {
                        connectingPlayers.invalidate(uuid)
                    }
                }
            }
        }
    }

    layoutTarget('L')

    settings {
        paginationViewRows(PaginationViewRows.ONE)
    }

    onFirstRender {
        if (SurfCoreApi.getServerByCategory(lobbyConfig.lobbyCategory).isEmpty()) {
            slot(5, 2) {
                withItem(buildItem(Material.BARRIER) {
                    displayName { error("Keine Lobby Server verfügbar") }
                })
            }
        }
    }
}

private val lobbySelectorItem
    get() = plugin.getInvisibleItem().apply {
        displayName {
            primary("Lobby Auswahl")
        }
    }

private val spawnItem
    get() = plugin.getInvisibleItem().apply {
        displayName {
            primary("Spawn")
        }
    }

private val rulesItem
    get() = plugin.getInvisibleItem().apply {
        displayName {
            primary("Regelwerk")
        }
    }

private val cosmeticsItem
    get() = plugin.getInvisibleItem().apply {
        displayName {
            primary("???")
        }
    }

private val survivalServerItem
    get() = plugin.getInvisibleItem().apply {
        displayName {
            primary("CASTSMP ")
            spacer("(Survival)")
        }

        buildLore {
            emptyLine()
            line {
                variableValue("Beschreibung".toSmallCaps())
            }
            line {
                white("Der ")
                info("CastSMP")
                white(" ist ein")
            }

            line {
                info("friedlicher")
                white(" Survival Server.")
            }

            line {
                white("Hier kannst du entspannt")
            }
            line {
                info("deine Träume")
                white(" verwirklichen.")
            }
        
            line {
                note("Status:".toSmallCaps())
            }
            line {
                when (SurfCoreApi.getServerByName(lobbyConfig.survivalServerName)?.state) {
                    SurfServerState.RUNNING -> success("Der CASTSMP ist erreichbar")
                    else -> error("Der CASTSMP ist derzeit nicht erreichbar")
                }
            }

            if (SurfCoreApi.getServerByName(lobbyConfig.survivalServerName)?.state == SurfServerState.RUNNING) {
                emptyLine()
                line {
                    note("Spieler:".toSmallCaps())
                }
                line {
                    val playerCount =
                        SurfCoreApi.getServerByName(lobbyConfig.survivalServerName)
                            ?.getPlayerCount() ?: -1
                    val maxPlayers =
                        SurfCoreApi.getServerByName(lobbyConfig.survivalServerName)
                            ?.maxPlayers ?: -1
                    info("$playerCount / $maxPlayers Spieler online")
                }
            }
        }
    }

private val eventServerItem
    get() = plugin.getInvisibleItem().apply {
        displayName {
            primary("Event Server")
        }

        buildLore {
            emptyLine()
            line {
                note("Status:".toSmallCaps())
            }
            line {
                when (eventServerBridge.state.get()) {
                    EventServerState.OPEN -> success("Klicke, um dem Event Server beizutreten")
                    EventServerState.CLOSED -> error("Der Event Server ist aktuell geschlossen")
                    EventServerState.UNKNOWN -> error("Aktuell findet kein Event statt")
                }
            }

            if (eventServerBridge.state.get() != EventServerState.UNKNOWN) {
                emptyLine()
                line {
                    note("Spieler:".toSmallCaps())
                }
                line {
                    val playerCount =
                        SurfCoreApi.getServerByName(lobbyConfig.eventServerName)
                            ?.getPlayerCount() ?: -1
                    val maxPlayers =
                        SurfCoreApi.getServerByName(lobbyConfig.eventServerName)
                            ?.maxPlayers ?: -1
                    info("$playerCount / $maxPlayers Spieler online")
                }
            }
        }
    }
