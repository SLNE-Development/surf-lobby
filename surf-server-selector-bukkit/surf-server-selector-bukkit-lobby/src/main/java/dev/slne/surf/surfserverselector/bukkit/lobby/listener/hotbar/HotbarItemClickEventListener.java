package dev.slne.surf.surfserverselector.bukkit.lobby.listener.hotbar;

import dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.PlayerHotbar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class HotbarItemClickEventListener implements Listener {

  @EventHandler
  public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
    if (!event.hasItem()) {
      return;
    }

    if (PlayerHotbar.handleItemClick(event.getPlayer(), event.getItem())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onInventoryClick(@NotNull InventoryClickEvent event) {
    if (!(event.getWhoClicked() instanceof Player clicker)) {
      return;
    }

    final ItemStack currentItem = event.getCurrentItem();
    if (currentItem != null && PlayerHotbar.handleItemClick(clicker, currentItem)) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
    if (PlayerHotbar.isHotbarItem(event.getPlayer(), event.getItemDrop().getItemStack())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerSwapHandItems(@NotNull PlayerSwapHandItemsEvent event) {
    final Player player = event.getPlayer();

    if (PlayerHotbar.isHotbarItem(player, event.getMainHandItem()) || PlayerHotbar.isHotbarItem(player, event.getOffHandItem())) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onInventoryDrag(InventoryDragEvent event) {
    if (!(event.getWhoClicked() instanceof Player clicker)) {
      return;
    }

    for (final ItemStack item : event.getNewItems().values()) {
      if (PlayerHotbar.isHotbarItem(clicker, item)) {
        event.setCancelled(true);
        return;
      }
    }
  }
}
