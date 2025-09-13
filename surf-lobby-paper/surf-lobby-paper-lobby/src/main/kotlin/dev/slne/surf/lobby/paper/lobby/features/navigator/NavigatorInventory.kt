package dev.slne.surf.lobby.paper.lobby.features.navigator

import dev.slne.surf.lobby.paper.lobby.features.navigator.lobby.lobbySwitcherInventory
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.menu
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.slot
import dev.slne.surf.surfapi.bukkit.api.inventory.dsl.staticPane
import dev.slne.surf.surfapi.bukkit.api.inventory.types.SurfChestGui
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import org.bukkit.inventory.ItemType


fun navigatorInventory() = menu(text("Navigator")) {
    survivalPane()
    roleplayPane()
    eventPane()
    gamesPane()
    homePane()
    lobbySwitcherPane()
    rulesPane()
}


private fun SurfChestGui.survivalPane() = staticPane(slot(0), 1, 2) {

}

private fun SurfChestGui.roleplayPane() = staticPane(slot(1), 1, 2) {

}

private fun SurfChestGui.eventPane() = staticPane(slot(2), 1, 2) {

}

private fun SurfChestGui.gamesPane() = staticPane(slot(3), 1, 2) {

}

private fun SurfChestGui.homePane() = staticPane(slot(4), 1, 2) {

}

private fun SurfChestGui.lobbySwitcherPane() = staticPane(slot(7), 1, 2) {
    fillWith(ItemType.PAPER.createItemStack()) { click ->
        lobbySwitcherInventory().show(click.whoClicked)
    }
}

private fun SurfChestGui.rulesPane() = staticPane(slot(5), 1, 2) {

}