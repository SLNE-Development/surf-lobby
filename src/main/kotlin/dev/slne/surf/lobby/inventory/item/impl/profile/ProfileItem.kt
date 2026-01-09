package dev.slne.surf.lobby.inventory.item.impl.profile

import dev.slne.surf.lobby.inventory.item.InventoryItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemType
import org.bukkit.inventory.meta.SkullMeta

object ProfileItem : InventoryItem(1, ItemType.PLAYER_HEAD.createItemStack().apply {
    applyDisplayAndLore()
}) {
    override val permission = null
    override fun onInteract(player: Player) {}
    
    override fun getItemForPlayer(player: Player) = ItemType.PLAYER_HEAD.createItemStack().apply {
        editMeta(SkullMeta::class.java) {
            it.owningPlayer = player
        }
        applyDisplayAndLore()
    }
    
    private fun ItemStack.applyDisplayAndLore() {
        displayName {
            localColored("Dein Profil")
        }

        buildLore {
            emptyLine()
            line {
                variableValue("Beschreibung:".toSmallCaps())
            }
            line {
                spacer("-")
                appendSpace()
                localColored("Bearbeite dein Profil")
            }

            line {
                spacer("-")
                appendSpace()
                localColored("Siehe deine Freunde an")
            }

            line {
                spacer("-")
                appendSpace()
                localColored("Neuste Informationen zu deinem Clan")
            }
            emptyLine()

            line {
                spacer("» Klicke, um dein Profil zu öffnen")
            }
        }
    }
}

private fun SurfComponentBuilder.localColored(text: Any, vararg decoration: TextDecoration) =
    text(text.toString(), TextColor.fromHexString("#42f590"), *decoration)