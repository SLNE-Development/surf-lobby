package dev.slne.surf.lobby.hook.hologram

import de.oliver.fancyholograms.api.FancyHologramsPlugin
import de.oliver.fancyholograms.api.data.TextHologramData
import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.minimessage.miniMessage
import dev.slne.surf.lobby.lobbyConfig
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.entity.Display

private val backgroundColor = Color.fromARGB(150, 0, 0, 0)

object HologramHook {
    private val hologramManager by lazy {
        FancyHologramsPlugin.get().hologramManager
    }

    fun reload() {
        hologramManager.holograms.filter { it.name.contains("surf-lobby") }.forEach {
            hologramManager.removeHologram(it)
        }

        init()
    }

    fun init() {
        val leftHologram =
            TextHologramData("surf-lobby-left", lobbyConfig.leftHologram.toLocation())

        val rightHologram =
            TextHologramData("surf-lobby-right", lobbyConfig.rightHologram.toLocation())

        leftHologram.setPersistent(false)
        rightHologram.setPersistent(false)
        leftHologram.setBillboard(Display.Billboard.FIXED)
        rightHologram.setBillboard(Display.Billboard.FIXED)
        leftHologram.removeLine(0)
        rightHologram.removeLine(0)
        leftHologram.visibilityDistance = 50
        rightHologram.visibilityDistance = 50
        leftHologram.setBackground(backgroundColor)
        rightHologram.setBackground(backgroundColor)

        miniMessage.serialize(leftText).split("\n").forEach {
            leftHologram.addLine(it)
        }

        miniMessage.serialize(rightText).split("\n").forEach {
            rightHologram.addLine(it)
        }

        hologramManager.addHologram(hologramManager.create(leftHologram))
        hologramManager.addHologram(hologramManager.create(rightHologram))
    }


    private val leftText = buildText {
        appendNewline()
        variableValue("CastSMP".toSmallCaps(), TextDecoration.BOLD)
        appendNewline(2)

        info("Du möchtest auf den CastSMP?")
        appendNewline()

        info("Nutze den ")
        white("Kompass in deinem Inventar")
        appendNewline()
        info("und wähle den Survival Server aus.")
        appendNewline(2)

        info("Du wirst automatisch zum ")
        white("Survival Schiff")
        appendNewline()
        info(" teleportiert.")
        appendNewline(2)

        info("Dort kannst du über ")
        white("einen Klick auf Nepomuk")
        appendNewline()
        info(" den Survival Server betreten.")
        appendNewline(2)

        note("server.castcrafter.de/rules")
        appendNewline()
    }

    private val rightText = buildText {
        appendNewline()
        variableValue("100 Spieler Events".toSmallCaps(), TextDecoration.BOLD)
        appendNewline(2)

        info("Du möchtest an Events teilnehmen?")
        appendNewline(2)

        error("Sobald ein Event stattfindet,")
        appendNewline()
        error("wird es im Discord angekündigt.")
        appendNewline(2)

        info("Nutze den ")
        white("Kompass in deinem Inventar")
        appendNewline()
        info("und wähle den Event Server aus.")
        appendNewline(2)

        info("Du wirst automatisch zum ")
        white("Event Schiff")
        appendNewline()
        info(" teleportiert.")
        appendNewline(2)

        info("Dort kannst du über ")
        white("einen Klick auf den NPC")
        appendNewline()
        info(" dem Event beitreten.")
        appendNewline(2)

        note("server.castcrafter.de/rules")
        appendNewline()
    }
}