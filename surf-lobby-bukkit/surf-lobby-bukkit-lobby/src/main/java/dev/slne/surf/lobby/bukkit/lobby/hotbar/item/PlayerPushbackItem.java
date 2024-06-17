package dev.slne.surf.lobby.bukkit.lobby.hotbar.item;

import dev.slne.surf.lobby.bukkit.common.util.ItemStackBuilder;
import dev.slne.surf.lobby.bukkit.lobby.features.PushbackManager;
import dev.slne.surf.surfapi.core.api.messages.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * The type Player pushback item.
 */
public class PlayerPushbackItem extends HotbarItem {

  private boolean enabled;

  /**
   * Instantiates a new Player pushback item.
   */
  public PlayerPushbackItem() {
    super("player-pushback", 2);
  }

  @Override
  protected ItemStackBuilder buildItem() {
    ItemStackBuilder builder = ItemStackBuilder.create(Material.ENDER_EYE);

    builder.withLore(Component.empty(), Component.text("Stößt nervige Spieler zurück"));
    builder.withDisplayName(Component.text("Spieler zurückstoßen", Colors.VARIABLE_VALUE));

    if(enabled) {
      builder.withGlint();
    }

    return builder;
  }

  /**
   * On click.
   */
  @Override
  public void onClick(Player player) {
    enabled = !enabled;

    PushbackManager.INSTANCE.togglePlayerPushback(player, enabled);
  }
}
