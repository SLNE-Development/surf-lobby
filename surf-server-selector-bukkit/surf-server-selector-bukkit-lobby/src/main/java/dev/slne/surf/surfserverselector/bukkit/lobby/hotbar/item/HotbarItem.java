package dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item;

import static dev.slne.surf.surfapi.bukkit.api.util.UtilBukkit.key;

import dev.slne.surf.surfserverselector.bukkit.common.util.ItemStackBuilder;
import dev.slne.surf.surfserverselector.bukkit.common.util.pdc.DataTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern.Value;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.springframework.data.util.Lazy;

public abstract class HotbarItem {

  public static final NamespacedKey HOTBAR_ITEM_KEY = key("hotbar-item");

  private final Lazy<ItemStack> item = Lazy.of(() -> buildItem()
      .withPdcModifier(pdc -> pdc.set(HOTBAR_ITEM_KEY, DataTypes.KEY, getKey()))
      .build());
  protected final Player player;
  private final Key key;

  public HotbarItem(Player player, @Value String key) {
    this(player, Key.key("hotbar-item", key));
  }

  public HotbarItem(Player player, Key key) {
    this.player = player;
    this.key = key;
  }

  protected abstract ItemStackBuilder buildItem();

  public abstract void onClick();

  public ItemStack getItem() {
    return item.get();
  }

  public Key getKey() {
    return key;
  }
}
