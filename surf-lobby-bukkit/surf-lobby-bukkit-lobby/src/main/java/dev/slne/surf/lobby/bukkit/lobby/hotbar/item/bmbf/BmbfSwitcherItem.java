package dev.slne.surf.lobby.bukkit.lobby.hotbar.item.bmbf;

import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.bukkit.common.settings.SettingManager;
import dev.slne.surf.lobby.bukkit.common.util.ItemStackBuilder;
import dev.slne.surf.lobby.bukkit.lobby.hotbar.item.HotbarItem;
import dev.slne.surf.lobby.core.message.Messages;
import dev.slne.surf.surfapi.core.api.messages.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public final class BmbfSwitcherItem extends HotbarItem {

  public BmbfSwitcherItem() {
    super("bmbf-switcher", 8, false);
  }

  @Override
  protected ItemStackBuilder buildItem() {
    return ItemStackBuilder.create(Material.COMPARATOR)
        .withDisplayName(Component.text("Crafting-Challenge", Colors.PRIMARY))
        .withLore(
            Component.empty(),
            Component.text("Gestalte die Energieversorgung", Colors.SECONDARY),
            Component.text("der Zukunft in Minecraft!", Colors.SECONDARY),
            Component.empty(),
            Component.text("Teilnahme: ", Colors.VARIABLE_KEY)
                .append(Component.text("Solo oder Team", Colors.VARIABLE_VALUE)),
            Component.text("Altersgrenze: ", Colors.VARIABLE_KEY)
                .append(Component.text("12+", Colors.VARIABLE_VALUE)),
            Component.text("Preise: ", Colors.VARIABLE_KEY)
                .append(Component.text("bis 300 €", Colors.VARIABLE_VALUE)),
            Component.empty(),
            Component.text("» Klicke, um beizutreten «", Colors.SUCCESS),
            Component.empty(),
            Component.text("30.05.–15.09.2025", Colors.INFO),
            Component.text("Veranstalter: BMFTR", Colors.SECONDARY),
            Component.empty()
        )
        .withGlint()
        .withItemFlag(ItemFlag.HIDE_ENCHANTS);
  }

  @Override
  public void onClick(Player player) {
    if (!SettingManager.getBmbfServerData().isEnabled()) {
      Messages.BMBF_EVENT_NOT_STARTED_YET.send(player);
      return;
    }

    LobbyApi.getPlayer(player.getUniqueId())
        .changeServer(SettingManager.getBmbfServerData().getBmbfServerName(), true);
  }
}
