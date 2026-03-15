package dev.slne.surf.lobby.listener

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.core.api.paper.util.surfPlayer
import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.lobby.manager.ElytraBoostManager
import dev.slne.surf.lobby.manager.PlayerVisibilityManager
import dev.slne.surf.lobby.manager.PushbackManager
import dev.slne.surf.lobby.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.playSound
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import kotlinx.coroutines.delay
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlin.time.Duration.Companion.seconds

object PlayerConnectionListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.gameMode = GameMode.ADVENTURE
        event.player.inventory.heldItemSlot = 4

        calcYearXp(event.player)

        for (i in 0..8) {
            event.player.inventory.clear(i)
        }

        event.player.inventory.chestplate = null


        val firstSeen = event.surfPlayer.firstSeen

        if (firstSeen == null || firstSeen.isAfter(OffsetDateTime.now().minusMinutes(5))) {
        }

        plugin.launch {
            playFirstJoinStuff(event.player)
        }

        InventoryItem.items.filter { item ->
            item.permission?.let { event.player.hasPermission(it) } ?: true
        }.forEach {
            event.player.inventory.setItem(it.slot, it.getItemForPlayer(event.player))
        }

        PlayerVisibilityManager.onPlayerJoin(event.player)
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent) {
        PushbackManager.remove(event.player.uniqueId)
        PlayerVisibilityManager.remove(event.player.uniqueId)
        ElytraBoostManager.clearBoost(event.player)
    }

    private fun calcYearXp(player: Player) {
        val today: LocalDate = LocalDate.now()

        val dayOfYear: Int = today.dayOfYear
        val daysInYear: Int = today.lengthOfYear()

        player.exp = dayOfYear.toFloat() / daysInYear
        player.level = today.year
    }

    private suspend fun playFirstJoinStuff(player: Player) {
        delay(3.seconds)
        player.sendText {
            appendInfoPrefix()
            info("Willkommen auf dem ")
            variableValue("CastCrafter Community Server ")
            variableValue("@${player.name}", TextDecoration.BOLD)
        }
        player.sendActionBar(buildText {
            appendInfoPrefix()
            info("Willkommen auf dem ")
            variableValue("CastCrafter Community Server ")
            variableValue("@${player.name}", TextDecoration.BOLD)
        })
        player.playSound(true) {
            type(Sound.BLOCK_NOTE_BLOCK_PLING)
        }

        delay(3.seconds)

        player.sendText {
            appendInfoPrefix()
            info("Du bist in der Lobby. Über den ")
            white("Kompass in deinem Inventar ")
            info("kannst du das Event- oder Survivalschiff betreten.")
            appendNewInfoPrefixedLine()
            info("Dort kannst du über den NPC den jeweiligen Modus betreten.")
        }
        player.sendActionBar(buildText {
            appendInfoPrefix()
            info("Du bist in der Lobby. Über den ")
            white("Kompass in deinem Inventar ")
            info("kannst du das Event- oder Survivalschiff betreten. ")
            info("Dort kannst du über den NPC den jeweiligen Modus betreten.")
        })
        player.playSound(true) {
            type(Sound.ENTITY_CHICKEN_EGG)
        }
        delay(1.seconds)

        player.sendText {
            appendInfoPrefix()
            info("In der Lobby kannst du außerdem andere Spieler verstecken oder dein Profil ansehen.")
        }
        player.sendActionBar(buildText {
            appendInfoPrefix()
            info("In der Lobby kannst du außerdem andere Spieler verstecken oder dein Profil ansehen.")
        })
        player.playSound(true) {
            type(Sound.ENTITY_CHICKEN_EGG)
        }
        delay(2.seconds)

        player.sendText {
            appendInfoPrefix()
            info("Nicht zu vergessen, ist der Lobby-Parkour, welchen du über die Schuhe in deinem Inventar betreten kannst.")
        }
        player.sendActionBar(buildText {
            appendInfoPrefix()
            info("Nicht zu vergessen, ist der Lobby-Parkour, welchen du über die Schuhe in deinem Inventar betreten kannst.")
        })
        player.playSound(true) {
            type(Sound.ENTITY_CHICKEN_EGG)
        }

        delay(3.seconds)

        player.sendText {
            appendInfoPrefix()
            info("Viel Spaß auf dem Server!")
        }
        player.sendActionBar(buildText {
            appendInfoPrefix()
            info("Viel Spaß auf dem Server!")
        })

        player.playSound(true) {
            type(Sound.ENTITY_PLAYER_LEVELUP)
        }
    }
}