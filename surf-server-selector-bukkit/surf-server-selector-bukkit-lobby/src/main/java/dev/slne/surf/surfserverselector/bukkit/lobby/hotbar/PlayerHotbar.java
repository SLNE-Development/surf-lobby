package dev.slne.surf.surfserverselector.bukkit.lobby.hotbar;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.surfserverselector.bukkit.common.util.pdc.DataTypes;
import dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item.HotbarItem;
import dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item.switcher.LobbySwitcherItem;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Optional;
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

  public boolean handleItemClick(@NotNull ItemStack itemStack) {
    checkNotNull(itemStack, "itemStack");

    final Optional<HotbarItem> hotbarItem = getItemByItemStack(itemStack);

    if (hotbarItem.isPresent()) {
      hotbarItem.get().onClick();
      return true;
    }

    return false;
  }

  public boolean isHotbarItem(@NotNull ItemStack itemStack) {
    checkNotNull(itemStack, "itemStack");

    return getItemByItemStack(itemStack).isPresent();
  }

  private Optional<HotbarItem> getItemByItemStack(@NotNull ItemStack itemStack) {
    return getKeyByItemStack(itemStack).flatMap(this::getItemByKey);
  }

  private Optional<Key> getKeyByItemStack(@NotNull ItemStack itemStack) {
    final ItemMeta meta = itemStack.getItemMeta();

    if (meta == null) {
      return Optional.empty();
    }

    final PersistentDataContainer pdc = meta.getPersistentDataContainer();

    return Optional.ofNullable(pdc.get(HotbarItem.HOTBAR_ITEM_KEY, DataTypes.KEY));
  }

  private @NotNull Optional<HotbarItem> getItemByKey(Key key) {
    return items.stream()
        .filter(item -> item.getKey().equals(key))
        .findFirst();
  }

  public static boolean handleItemClick(Player player, ItemStack item) {
    final PlayerHotbar hotbar = HOTBARS.get(player);

    return hotbar != null && hotbar.handleItemClick(item);
  }

  public static void destroyHotbar(@NotNull Player player) {
    HOTBARS.remove(player);
  }

  public static boolean isHotbarItem(@NotNull Player player, @NotNull ItemStack item) {
    final PlayerHotbar hotbar = HOTBARS.get(player);

    return hotbar != null && hotbar.isHotbarItem(item);
  }
}
