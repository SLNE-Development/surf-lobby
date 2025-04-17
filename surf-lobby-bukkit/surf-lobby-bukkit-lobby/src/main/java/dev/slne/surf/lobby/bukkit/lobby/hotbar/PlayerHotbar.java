package dev.slne.surf.lobby.bukkit.lobby.hotbar;

import static com.google.common.base.Preconditions.checkNotNull;

import dev.slne.surf.lobby.bukkit.common.util.pdc.DataTypes;
import dev.slne.surf.lobby.bukkit.lobby.hotbar.item.HotbarItem;
import dev.slne.surf.lobby.bukkit.lobby.hotbar.item.PlayerPushbackItem;
import dev.slne.surf.lobby.bukkit.lobby.hotbar.item.bmbf.BmbfSwitcherItem;
import dev.slne.surf.lobby.bukkit.lobby.hotbar.item.switcher.LobbySwitcherItem;
import dev.slne.surf.lobby.core.permissions.Permissions;
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
import org.jetbrains.annotations.NotNull;

/**
 * The type Player hotbar.
 */
public final class PlayerHotbar {

  private static final Object2ObjectMap<Player, PlayerHotbar> HOTBARS = Object2ObjectMaps.synchronize(
      new Object2ObjectOpenHashMap<>());

  private final Player player;
  private final PlayerInventory inv;
  private final ObjectList<HotbarItem> items;

  /**
   * Instantiates a new Player hotbar.
   *
   * @param player the player
   */
  public PlayerHotbar(@NotNull Player player) {
    this.player = player;
    this.inv = player.getInventory();
    this.items = new ObjectArrayList<>();

    HOTBARS.put(player, this);
  }

  /**
   * Populate hotbar.
   */
  public void populateHotbar() {
    inv.clear();

    item(new LobbySwitcherItem());
    item(new BmbfSwitcherItem());

    if(player.hasPermission(Permissions.FEATURE_USE_PLAYER_PUSHBACK_ITEM.getPermission())) {
      item(new PlayerPushbackItem());
    }
  }

  /**
   * Item.
   *
   * @param item the item
   */
  private void item(@NotNull HotbarItem item) {
    inv.setItem(item.getX(), item.buildWithKey().build());

    items.add(item);
  }

  /**
   * Handle item click boolean.
   *
   * @param player    the player
   * @param itemStack the item stack
   * @return the boolean
   */
  public boolean handleItemClick(Player player, @NotNull ItemStack itemStack) {
    checkNotNull(itemStack, "itemStack");

    final Optional<HotbarItem> hotbarItem = getItemByItemStack(itemStack);

    if (hotbarItem.isPresent()) {
      HotbarItem item = hotbarItem.get();

      item.onClick(player);

      if(item.isUpdatable()) {
        player.getInventory().setItem(item.getX(), item.buildWithKey().build());
      }

      return true;
    }

    return false;
  }

  /**
   * Is hotbar item boolean.
   *
   * @param itemStack the item stack
   * @return the boolean
   */
  public boolean isHotbarItem(@NotNull ItemStack itemStack) {
    checkNotNull(itemStack, "itemStack");

    return getItemByItemStack(itemStack).isPresent();
  }

  /**
   * Gets item by item stack.
   *
   * @param itemStack the item stack
   * @return the item by item stack
   */
  private Optional<HotbarItem> getItemByItemStack(@NotNull ItemStack itemStack) {
    return getKeyByItemStack(itemStack).flatMap(this::getItemByKey);
  }

  /**
   * Gets key by item stack.
   *
   * @param itemStack the item stack
   * @return the key by item stack
   */
  private Optional<Key> getKeyByItemStack(@NotNull ItemStack itemStack) {
    final ItemMeta meta = itemStack.getItemMeta();

    if (meta == null) {
      return Optional.empty();
    }

    return Optional.ofNullable(meta.getPersistentDataContainer().get(HotbarItem.HOTBAR_ITEM_KEY, DataTypes.KEY));
  }

  /**
   * Gets item by key.
   *
   * @param key the key
   * @return the item by key
   */
  private @NotNull Optional<HotbarItem> getItemByKey(Key key) {
    return items.stream()
        .filter(item -> item.getKey().equals(key))
        .findFirst();
  }

  /**
   * Handle static item click boolean.
   *
   * @param player the player
   * @param item   the item
   * @return the boolean
   */
  public static boolean handleStaticItemClick(Player player, ItemStack item) {
    final PlayerHotbar hotbar = HOTBARS.get(player);

    return hotbar != null && hotbar.handleItemClick(player, item);
  }

  /**
   * Destroy hotbar.
   *
   * @param player the player
   */
  public static void destroyHotbar(@NotNull Player player) {
    HOTBARS.remove(player);
  }

  /**
   * Is hotbar item boolean.
   *
   * @param player the player
   * @param item   the item
   * @return the boolean
   */
  public static boolean isHotbarItem(@NotNull Player player, @NotNull ItemStack item) {
    final PlayerHotbar hotbar = HOTBARS.get(player);

    return hotbar != null && hotbar.isHotbarItem(item);
  }
}
