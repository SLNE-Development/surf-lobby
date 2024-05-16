package dev.slne.surf.surfserverselector.bukkit.hotbar.item.lobby;

import dev.slne.surf.surfapi.core.api.messages.Colors;
import dev.slne.surf.surfserverselector.bukkit.hotbar.item.HotbarItem;
import dev.slne.surf.surfserverselector.bukkit.util.ItemStackBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class LobbySwitcherItem extends HotbarItem {

  public LobbySwitcherItem(Player player) {
    super(player, "lobby-switcher");
  }

  @Override
  protected ItemStackBuilder buildItem() {
    return ItemStackBuilder.create(Material.COMPASS)
        .withDisplayName(Component.text("Lobby Switcher", Colors.PRIMARY))
        .withLore(
            Component.empty(),
            Component.empty(),
            Component.text("Klicke um die Lobby zu wechseln.", Colors.SECONDARY),
            Component.empty(),
            Component.empty()
        );
  }

  @Override
  public void onClick() {
    player.sendPlainMessage("Switch lobby");
    // TODO: 16.05.2024 10:25 - implement
  }
}
