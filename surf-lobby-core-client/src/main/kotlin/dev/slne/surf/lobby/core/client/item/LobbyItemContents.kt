package dev.slne.surf.lobby.core.client.item

import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.`object`.ObjectContents
import java.util.*

/**
 * The names and lore lines of the lobby hotbar items, shared between the platforms.
 */
object LobbyItemContents {

    object Navigator {
        const val SLOT = 4

        private val color = TextColor.color(0xf58442)

        val name = buildText { localColored(color, "Navigator") }

        val lore = arrayOf(
            emptyLine(),
            buildText { variableValue("Beschreibung:".toSmallCaps()) },
            dashedLine(color, "Teleport zum Event-Schiff"),
            dashedLine(color, "Teleport zum Survival-Schiff"),
            emptyLine(),
            buildText { spacer("» Klicke, um den Navigator zu öffnen") }
        )
    }

    object Parkour {
        const val SLOT = 1

        private val color = TextColor.color(0x03fcc6)

        val name = buildText { localColored(color, "Parkour") }

        val lore = arrayOf(
            emptyLine(),
            buildText { variableValue("Beschreibung:".toSmallCaps()) },
            dashedLine(color, "Starte den Lobby Parkour"),
            dashedLine(color, "Siehe Statistiken an"),
            dashedLine(color, "Stelle neue Rekorde auf"),
            emptyLine(),
            buildText { spacer("» Klicke, um das Parkour Menu zu öffnen") }
        )
    }

    object Profile {
        const val SLOT = 8

        private val color = TextColor.color(0x42f590)

        fun name(playerUuid: UUID) = buildText {
            append(
                Component.`object`(ObjectContents.playerHead(playerUuid))
            ).color(NamedTextColor.WHITE)
            appendSpace()
            localColored(color, "Dein Profil")
        }

        val lore = arrayOf(
            emptyLine(),
            buildText { variableValue("Beschreibung:".toSmallCaps()) },
            dashedLine(color, "Bearbeite dein Profil"),
            dashedLine(color, "Siehe deine Freunde an"),
            dashedLine(color, "Neuste Informationen zu deinem Clan"),
            emptyLine(),
            buildText { spacer("» Klicke, um dein Profil zu öffnen") }
        )
    }

    object Trophies {
        const val SLOT = 7

        private val color = TextColor.color(0xffe700)

        val name = buildText { localColored(color, "Trophäen") }

        val lore = arrayOf(
            emptyLine(),
            buildText { variableValue("Beschreibung:".toSmallCaps()) },
            dashedLine(color, "Siehe deine Erfolge an"),
            dashedLine(color, "Erkunde alle Trophäen"),
            emptyLine(),
            buildText { spacer("» Klicke, um deine Erfolge zu öffnen") }
        )
    }

    object Pushback {
        const val SLOT = 2

        private val color = TextColor.color(0xf5426c)

        val name = buildText { variableValue("Pushback") }

        val enabledLore = lore(
            buildText {
                appendSpace()
                spacer("-")
                appendSpace()
                success("Aktiviert", TextDecoration.BOLD)
            },
            buildText {
                spacer("-")
                appendSpace()
                error("Deaktiviert")
            }
        )

        val disabledLore = lore(
            buildText {
                spacer("-")
                appendSpace()
                error("Aktiviert")
            },
            buildText {
                appendSpace()
                spacer("-")
                appendSpace()
                success("Deaktiviert", TextDecoration.BOLD)
            }
        )

        private fun lore(vararg statusLines: Component) = arrayOf(
            emptyLine(),
            buildText { variableValue("Beschreibung:".toSmallCaps()) },
            dashedLine(color, "Pushback steuern"),
            dashedLine(color, "Pushback aktivieren oder deaktivieren"),
            emptyLine(),
            buildText { variableValue("Status".toSmallCaps()) },
            *statusLines,
            emptyLine(),
            buildText { spacer("» Klicke, um zu wechseln") }
        )
    }

    object Visibility {
        const val SLOT = 0

        private val color = TextColor.color(0x00d0fa)

        val name = buildText { localColored(color, "Spieler-Sichtbarkeit") }

        val showAllLore = lore(
            buildText {
                appendSpace()
                spacer("-")
                appendSpace()
                success("Alle Spieler", TextDecoration.BOLD)
            },
            buildText {
                spacer("-")
                appendSpace()
                error("Nur Teammitglieder")
            },
            buildText {
                spacer("-")
                appendSpace()
                error("Keine Spieler")
            }
        )

        val showTeamLore = lore(
            buildText {
                spacer("-")
                appendSpace()
                error("Alle Spieler")
            },
            buildText {
                appendSpace()
                spacer("-")
                appendSpace()
                success("Nur Teammitglieder", TextDecoration.BOLD)
            },
            buildText {
                spacer("-")
                appendSpace()
                error("Keine Spieler", TextDecoration.BOLD)
            }
        )

        val showNoneLore = lore(
            buildText {
                spacer("-")
                appendSpace()
                error("Alle Spieler")
            },
            buildText {
                spacer("-")
                appendSpace()
                error("Nur Teammitglieder")
            },
            buildText {
                appendSpace()
                spacer("-")
                appendSpace()
                success("Keine Spieler", TextDecoration.BOLD)
            }
        )

        private fun lore(vararg statusLines: Component) = arrayOf(
            emptyLine(),
            buildText { variableValue("Beschreibung:".toSmallCaps()) },
            dashedLine(color, "Spielersichtbarkeit steuern"),
            dashedLine(
                color,
                "Zwischen allen Spielern, Teammitgliedern und keinen Spielern wechseln"
            ),
            emptyLine(),
            buildText { variableValue("Status".toSmallCaps()) },
            *statusLines,
            emptyLine(),
            buildText { spacer("» Klicke, um zu wechseln") }
        )
    }

    object ElytraBoost {
        val name = buildText { variableValue("Elytra Boost") }
    }

    private fun emptyLine(): Component = Component.empty()

    private fun dashedLine(color: TextColor?, text: String) = buildText {
        spacer("-")
        appendSpace()
        localColored(color, text)
    }

    private fun SurfComponentBuilder.localColored(
        color: TextColor?,
        text: Any,
        vararg decoration: TextDecoration
    ) = text(text.toString(), color, *decoration)
}
