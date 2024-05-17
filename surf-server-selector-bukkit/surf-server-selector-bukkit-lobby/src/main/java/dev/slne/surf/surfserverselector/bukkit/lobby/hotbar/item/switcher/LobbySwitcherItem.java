package dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item.switcher;

import dev.slne.surf.surfapi.core.api.messages.Colors;
import dev.slne.surf.surfserverselector.bukkit.common.util.ItemStackBuilder;
import dev.slne.surf.surfserverselector.bukkit.lobby.BukkitMain;
import dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item.HotbarItem;
import me.devnatan.inventoryframework.ViewFrame;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.CompassMeta;

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
        )
        .withMetaModifier(CompassMeta.class, meta -> meta.setLodestone(null))
        .withGlint()
        .withItemFlag(ItemFlag.HIDE_ENCHANTS);
  }

  @Override
  public void onClick() {
    player.sendPlainMessage("Switch lobby");
    ViewFrame viewFrame = BukkitMain.getInstance().getViewFrame();

    System.out.println(viewFrame.getRegisteredViewByType(LobbySwitcherView.class));

    Bukkit.getScheduler().runTask(BukkitMain.getInstance(), () -> {
      System.out.println(viewFrame.open(LobbySwitcherView.class, player));
    });
  }
}
