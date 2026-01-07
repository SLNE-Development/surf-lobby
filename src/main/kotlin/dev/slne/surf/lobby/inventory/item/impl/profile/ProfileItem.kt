package dev.slne.surf.lobby.inventory.item.impl.profile

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import org.bukkit.inventory.ItemType

object ProfileItem : InventoryItem(0, ItemType.PLAYER_HEAD.createItemStack().apply {
    displayName {
        variableValue("Dein Profil")
    }

    buildLore {
        emptyLine()
        line {
            variableValue("Beschreibung:".toSmallCaps())
        }
        line {
            spacer("-")
            appendSpace()
            note("Bearbeite dein Profil")
        }

        line {
            spacer("-")
            appendSpace()
            note("Siehe deine Freunde an")
        }

        line {
            spacer("-")
            appendSpace()
            note("Neuste Informationen zu deinem Clan")
        }
        emptyLine()

        line {
            spacer("» Klicke, um dein Profil zu öffnen")
        }
    }
})