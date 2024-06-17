package dev.slne.surf.lobby.bukkit.lobby.hotbar.item;

import static dev.slne.surf.surfapi.bukkit.api.util.UtilBukkit.key;

import dev.slne.surf.lobby.bukkit.common.util.ItemStackBuilder;
import dev.slne.surf.lobby.bukkit.common.util.pdc.DataTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern.Value;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

/**
 * The type Hotbar item.
 */
public abstract class HotbarItem {

  public static final NamespacedKey HOTBAR_ITEM_KEY = key("hotbar-item");

  private final Key key;
  private final int x;
  private final boolean updatable;

  /**
   * Instantiates a new Hotbar item.
   *
   * @param key       the key
   * @param x         the x
   * @param updatable the updatable
   */
  public HotbarItem(@Value String key, int x, boolean updatable) {
    this(Key.key("hotbar-item", key), x, updatable);
  }

  /**
   * Instantiates a new Hotbar item.
   *
   * @param key the key
   * @param x   the x
   */
  public HotbarItem(@Value String key, int x) {
    this(Key.key("hotbar-item", key), x, true);
  }

  /**
   * Instantiates a new Hotbar item.
   *
   * @param key       the key
   * @param x         the x
   * @param updatable the updatable
   */
  public HotbarItem(Key key, int x, boolean updatable) {
    this.key = key;
    this.x = x;
    this.updatable = updatable;
  }

  /**
   * Instantiates a new Hotbar item.
   *
   * @param key the key
   * @param x   the x
   */
  public HotbarItem(Key key, int x) {
    this(key, x, true);
  }

  /**
   * Build item item stack builder.
   *
   * @return the item stack builder
   */
  protected abstract ItemStackBuilder buildItem();

  /**
   * Build internal item item stack builder.
   *
   * @return the item stack builder
   */
  public ItemStackBuilder buildWithKey() {
    return buildItem()
        .withPdcModifier(pdc -> pdc.set(HOTBAR_ITEM_KEY, DataTypes.KEY, getKey()));
  }

  /**
   * On click.
   *
   * @param player the player
   */
  public abstract void onClick(Player player);

  /**
   * Gets key.
   *
   * @return the key
   */
  public Key getKey() {
    return key;
  }

  /**
   * Gets x.
   *
   * @return the x
   */
  public int getX() {
    return x;
  }

  /**
   * Is updatable boolean.
   *
   * @return the boolean
   */
  public boolean isUpdatable() {
    return updatable;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
