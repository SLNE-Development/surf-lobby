package dev.slne.surf.lobby.inventory.impl

import com.github.stefvanschie.inventoryframework.pane.util.Slot
import dev.slne.surf.lobby.plugin
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.bukkit.api.event.cancel
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.menu
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.staticPane
import dev.slne.surf.surfapi.core.api.messages.adventure.text

fun navigatorInventory() = menu(text("<shift:-46><glyph:server_selector>"), 6) {
    setOnGlobalDrag { it.cancel() }
    setOnGlobalClick { it.cancel() }

    staticPane(Slot.fromXY(5, 0), 3, 3) {
        fillWith(eventServerItem)
    }

    staticPane(Slot.fromXY(1, 0), 3, 3) {
        fillWith(survivalServerItem)
    }

    staticPane(Slot.fromXY(0, 4), 2, 3) {
        fillWith(lobbyOneServerItem)
    }

    staticPane(Slot.fromXY(3, 4), 2, 3) {
        fillWith(lobbyTwoServerItem)
    }
    staticPane(Slot.fromXY(6, 4), 2, 3) {
        fillWith(lobbyThreeServerItem)
    }
}

private val survivalServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Survival Server")
    }
}

private val eventServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Event Server")
    }
}

private val lobbyOneServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Lobby 1")
    }
}

private val lobbyTwoServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Lobby 2")
    }
}

private val lobbyThreeServerItem = plugin.getInvisibleItem().apply {
    displayName {
        primary("Lobby 3")
    }
}