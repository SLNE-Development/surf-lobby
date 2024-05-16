package dev.slne.surf.surfserverselector.bukkit.listener.lobby;

import dev.slne.surf.surfserverselector.bukkit.hotbar.PlayerHotbar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public final class HotbarItemClickEventListener implements Listener {

  @EventHandler
  public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
    if (!event.hasItem()) {
      return;
    }

    PlayerHotbar.handleItemClick(event.getPlayer(), event.getItem());
  }

  @EventHandler
  public void onInventoryClick(@NotNull InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player clicker)) {
      return;
    }

    PlayerHotbar.handleItemClick(clicker, event.getCurrentItem());
  }
}
