package dev.slne.surf.surfserverselector.bukkit.hotbar;

import dev.slne.surf.surfserverselector.bukkit.hotbar.item.HotbarItem;
import dev.slne.surf.surfserverselector.bukkit.hotbar.item.lobby.LobbySwitcherItem;
import dev.slne.surf.surfserverselector.bukkit.util.pdc.DataTypes;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public final class PlayerHotbar {

  private static final Object2ObjectMap<Player, PlayerHotbar> HOTBARS = Object2ObjectMaps.synchronize(
      new Object2ObjectOpenHashMap<>());

  private final Player player;
  private final PlayerInventory inv;
  private final ObjectList<HotbarItem> items;

  public PlayerHotbar(@NotNull Player player) {
    this.player = player;
    this.inv = player.getInventory();
    this.items = new ObjectArrayList<>();
    HOTBARS.put(player, this);
  }

  public void populateHotbar() {
    inv.clear();
    item(0, new LobbySwitcherItem(player));
  }

  private void item(int slot, @NotNull HotbarItem item) {
    inv.setItem(slot, item.getItem());
    items.add(item);
  }

  public void handleItemClick(@NotNull ItemStack item) {
    final ItemMeta meta = item.getItemMeta();

    if (meta == null) {
      return;
    }

    final PersistentDataContainer pdc = meta.getPersistentDataContainer();
    final Key key = pdc.get(HotbarItem.HOTBAR_ITEM_KEY, DataTypes.KEY);
    if (key == null) {
      return;
    }

    items.stream()
        .filter(hotbarItem -> hotbarItem.getKey().equals(key))
        .findFirst()
        .ifPresent(HotbarItem::onClick);
  }

  public static void handleItemClick(Player player, ItemStack item) {
    final PlayerHotbar hotbar = HOTBARS.get(player);
    if (hotbar != null) {
      hotbar.handleItemClick(item);
    }
  }

  public static void destroyHotbar(@NotNull Player player) {
    HOTBARS.remove(player);
  }
}
