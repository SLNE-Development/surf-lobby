package dev.slne.surf.lobby.minestom.npc

import dev.slne.minestom.lobby.api.extension.SchedulerManager
import dev.slne.minestom.lobby.api.npc.MannequinNpc
import dev.slne.minestom.lobby.api.npc.mannequinNpc
import dev.slne.surf.api.core.generated.ItemTypeKeys
import dev.slne.surf.api.core.messages.adventure.playSound
import dev.slne.surf.lobby.core.client.config.lobbyConfig
import dev.slne.surf.lobby.core.client.event.EventJoinAction
import dev.slne.surf.lobby.core.client.event.EventServerDisplayName
import dev.slne.surf.lobby.core.client.event.decideEventJoin
import dev.slne.surf.lobby.core.client.location.LobbyLocations
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendEventServerClosed
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendNoEventRunning
import dev.slne.surf.lobby.core.client.message.LobbyMessages.sendNpcRules
import dev.slne.surf.lobby.core.client.npc.LobbyNpcContents
import dev.slne.surf.lobby.core.client.npc.LobbyNpcSkins
import dev.slne.surf.lobby.core.client.queue.LobbyQueue
import dev.slne.surf.lobby.minestom.LobbyMinestomEntrypoint
import dev.slne.surf.lobby.minestom.location.toPos
import net.minestom.server.component.DataComponents
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EquipmentSlot
import net.minestom.server.entity.Player
import net.minestom.server.entity.PlayerSkin
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.network.player.ResolvableProfile
import net.minestom.server.sound.SoundEvent
import net.minestom.server.timer.Task
import java.time.temporal.ChronoUnit

object LobbyNpcs {
    private const val NPC_SCALE = 1.5

    lateinit var survivalNpc: EquipableMannequinNpc
    lateinit var eventNpc: EquipableMannequinNpc

    lateinit var spawnRulesNpc: MannequinNpc
    lateinit var spawnSurvivalNpc: MannequinNpc
    lateinit var spawnEventNpc: MannequinNpc
    lateinit var shopNpc: MannequinNpc

    fun spawnAll() {
        val instance = LobbyMinestomEntrypoint.lobbyInstance

        survivalNpc =
            equipableMannequinNpc("survival", instance, LobbyLocations.SURVIVAL_NPC.toPos()) {
                displayName = LobbyNpcContents.survivalNpcName
                profile = LobbyNpcSkins.SURVIVAL.toResolvableProfile()
                scale = NPC_SCALE

                onInteract {
                    LobbyQueue.queueToSurvivalServer(it.player.uuid)
                }

                withEquipment(
                    EquipmentSlot.MAIN_HAND,
                    ItemStack.builder(Material.fromKey(ItemTypeKeys.PAPER)).set(
                        DataComponents.ITEM_MODEL, "nexo:astronaut-hat"
                    ).build()
                )
            }

        eventNpc = equipableMannequinNpc("event", instance, LobbyLocations.EVENT_NPC.toPos()) {
            displayName = LobbyNpcContents.eventNpcName(EventServerDisplayName.current)
            profile = LobbyNpcSkins.EVENT.toResolvableProfile()
            scale = NPC_SCALE

            onInteract {
                val player = it.player

                when (decideEventJoin(player.uuid)) {
                    EventJoinAction.EXTERNAL_TELEPORT -> player.teleportWithSound(
                        lobbyConfig.externalEventTeleportLocation.toPos(),
                        pitch = 1.1f
                    )

                    EventJoinAction.QUEUE -> LobbyQueue.queueToEventServer(player.uuid)

                    EventJoinAction.CLOSED_MESSAGE -> player.sendEventServerClosed()

                    EventJoinAction.NO_EVENT_MESSAGE -> player.sendNoEventRunning()
                }
            }

            withEquipment(
                EquipmentSlot.MAIN_HAND,
                ItemStack.builder(Material.fromKey(ItemTypeKeys.PAPER)).set(
                    DataComponents.ITEM_MODEL, "nexo:stoned-pickaxe"
                ).build()
            )
        }

        spawnRulesNpc = mannequinNpc("spawn_rules", instance, LobbyLocations.SPAWN_RULES.toPos()) {
            displayName = LobbyNpcContents.rulesNpcName
            profile = LobbyNpcSkins.RULES.toResolvableProfile()
            scale = NPC_SCALE

            onInteract {
                it.player.sendNpcRules()
            }
        }

        spawnSurvivalNpc =
            mannequinNpc("spawn_survival", instance, LobbyLocations.SPAWN_SURVIVAL.toPos()) {
                displayName = LobbyNpcContents.spawnSurvivalNpcName
                profile = LobbyNpcSkins.SURVIVAL.toResolvableProfile()
                scale = NPC_SCALE

                onInteract {
                    it.player.teleportWithSound(
                        LobbyLocations.SURVIVAL_TELEPORT.toPos(),
                        pitch = 1.5f
                    )
                }
            }

        spawnEventNpc = mannequinNpc("spawn_event", instance, LobbyLocations.SPAWN_EVENT.toPos()) {
            displayName = LobbyNpcContents.spawnEventNpcName
            profile = LobbyNpcSkins.EVENT.toResolvableProfile()
            scale = NPC_SCALE

            onInteract {
                val player = it.player

                if (lobbyConfig.externalEventEnabled && lobbyConfig.externalEventReplacesDefault) {
                    player.teleportWithSound(
                        lobbyConfig.externalEventTeleportLocation.toPos(),
                        pitch = 1.1f
                    )
                    return@onInteract
                }

                player.teleportWithSound(LobbyLocations.EVENT_TELEPORT.toPos(), pitch = 1.5f)
            }
        }

        shopNpc = mannequinNpc("shop", instance, LobbyLocations.SPAWN_SHOP.toPos()) {
            displayName = LobbyNpcContents.shopNpcName
            profile = LobbyNpcSkins.UNKNOWN.toResolvableProfile()
            scale = NPC_SCALE
        }
    }

    private fun Player.teleportWithSound(
        pos: Pos,
        pitch: Float
    ) {
        teleport(pos).thenRun {
            playSound(true) {
                type(SoundEvent.ENTITY_ENDERMAN_TELEPORT)
                pitch(pitch)
            }
        }
    }

    private fun LobbyNpcSkins.toResolvableProfile() =
        ResolvableProfile(PlayerSkin(skinValue, skinSignature))

    @Volatile
    private var syncTask: Task? = null

    fun startSyncTask() {
        syncTask = SchedulerManager.buildTask {
            val displayName = EventServerDisplayName.refresh()

            if (displayName != null) {
                eventNpc.updateDisplayName(LobbyNpcContents.eventNpcName(displayName))
            }
        }.repeat(30, ChronoUnit.SECONDS).schedule()
    }

    fun stopSyncTask() {
        syncTask?.cancel()
        syncTask = null
    }
}
