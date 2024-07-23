package dev.slne.surf.lobby.bukkit.lobby.hotbar.item.selector;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.bukkit.common.settings.SettingManager;
import dev.slne.surf.lobby.bukkit.lobby.BukkitMain;
import dev.slne.surf.lobby.core.message.Messages;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.surfapi.core.api.messages.Colors;
import io.th0rgal.oraxen.api.OraxenItems;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SelectorGui extends ChestGui {

  private static final String INVISIBLE_ITEM_ID = "invisible_item";
  private static final BukkitMain plugin = BukkitMain.getInstance();

  public SelectorGui() {
    super(6, "<shift:-46><glyph:server_selector2>", BukkitMain.getInstance());

    setOnGlobalClick(cancel());
    setOnGlobalDrag(cancel());

    setupPane(createStaticPane(1, 0, 3, 2), createEventServerSwitchItem(1), switchEventServer(1));
    setupPane(createStaticPane(5, 0, 3, 2), createEventServerSwitchItem(2), switchEventServer(2));
    setupPane(createStaticPane(1, 3, 3, 2), createEventServerSwitchItem(3), switchEventServer(3));
    setupPane(createStaticPane(5, 3, 3, 2), createEventServerSwitchItem(4), switchEventServer(4));
  }

  private void setupPane(StaticPane pane, ItemStack item,
      Consumer<InventoryClickEvent> clickHandler) {
    pane.fillWith(item, null, plugin);
    pane.setOnClick(clickHandler);
    addPane(pane);
  }

  @SuppressWarnings("SameParameterValue")
  private StaticPane createStaticPane(int x, int y, int length, int height) {
    return new StaticPane(x, y, length, height);
  }

  private ItemStack createEventServerSwitchItem(int eventServerId) {
    final ItemStack item = createInvisibleItem();

    item.editMeta(itemMeta -> {
      EventServerData eventServerData;

      switch (eventServerId) {
        case 1 -> eventServerData = SettingManager.getEventServerOneData();
        case 2 -> eventServerData = SettingManager.getEventServerTwoData();
        case 3 -> eventServerData = SettingManager.getEventServerThreeData();
        case 4 -> eventServerData = SettingManager.getEventServerFourData();
        default -> eventServerData = SettingManager.getEventServerData();
      }

      final List<Component> lore = new ArrayList<>(3);

      if (eventServerData.isEventServerEnabled()) {
        lore.add(createPlayersOnlineLore(eventServerData.getOnlinePlayers(), eventServerData.getMaxPlayers()));
        lore.add(empty());
      }

      lore.add(eventServerData.isEventServerEnabled()
          ? text("Klicke um Server " + eventServerId + " beizutreten.")
          : text("Der Event Server ist derzeit deaktiviert.", Colors.ERROR));

      itemMeta.displayName(createNonItalicComponent("Event Server " + eventServerId, Colors.PRIMARY));
      itemMeta.lore(createNonItalicLore(lore.toArray(Component[]::new)));
    });

    return item;
  }

  private ItemStack createInvisibleItem() {
    return OraxenItems.getItemById(INVISIBLE_ITEM_ID).build().clone();
  }

  @SuppressWarnings("SameParameterValue")
  private Component createNonItalicComponent(String text, TextColor color) {
    return text(text, color).decoration(TextDecoration.ITALIC, false);
  }

  private List<? extends Component> createNonItalicLore(Component... lines) {
    final List<Component> lore = new ArrayList<>(lines.length + 2);
    lore.add(empty());
    lore.addAll(Arrays.stream(lines)
        .map(line -> line.colorIfAbsent(Colors.INFO).decoration(TextDecoration.ITALIC, false))
        .toList());
    lore.add(empty());

    return lore;
  }

  private @NotNull Component createPlayersOnlineLore(int onlinePlayers, int maxPlayers) {
    return text("Spieler: ", Colors.INFO)
        .append(text(onlinePlayers, Colors.VARIABLE_VALUE))
        .append(text(" / ", Colors.INFO))
        .append(text(maxPlayers, Colors.VARIABLE_VALUE));
  }

  @Contract(pure = true)
  private @NotNull Consumer<InventoryClickEvent> switchEventServer(int eventServerId) {
    return toPlayer(player -> {
      if (!SettingManager.getEventServerData().isEventServerEnabled()) {
        Messages.EVENT_SERVER_DISABLED.send(player);
        return;
      }

      LobbyApi.getPlayer(player.getUniqueId())
          .changeServer(SettingManager.getEventServerData().getEventServerName() + "-" + eventServerId, true);
    });
  }

  @Contract(pure = true)
  private @NotNull Consumer<InventoryClickEvent> toPlayer(Consumer<Player> consumer) {
    return event -> {
      if (event.getWhoClicked() instanceof Player player) {
        player.closeInventory();
        consumer.accept(player);
      }
    };
  }

  @Contract(pure = true)
  private <T extends Cancellable> @NotNull Consumer<T> cancel() {
    return event -> event.setCancelled(true);
  }
}
