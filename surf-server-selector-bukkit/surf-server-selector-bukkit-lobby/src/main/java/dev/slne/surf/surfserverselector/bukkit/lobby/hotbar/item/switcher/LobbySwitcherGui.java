package dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item.switcher;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.slne.surf.surfapi.core.api.messages.Colors;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.bukkit.common.settings.SettingManager;
import dev.slne.surf.surfserverselector.bukkit.lobby.BukkitMain;
import dev.slne.surf.surfserverselector.core.message.Messages;
import dev.slne.surf.surfserverselector.core.util.ListUtil;
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
import org.jetbrains.annotations.Nullable;

public final class LobbySwitcherGui extends ChestGui {

  private static final String INVISIBLE_ITEM_ID = "invisible_item";
  private final BukkitMain plugin = BukkitMain.getInstance();

  public LobbySwitcherGui() {
    super(6, "<shift:-8><glyph:server_selector>", BukkitMain.getInstance());

    setOnGlobalClick(cancel());
    setOnGlobalDrag(cancel());

    // @formatter:off
    setupPane(createStaticPane(5, 0, 3, 3), createEventServerSwitchItem(), switchEventServer());
    setupPane(createStaticPane(1, 0, 3, 3), createCommunityServerSwitchItem(), switchCommunityServer());
    setupPane(createStaticPane(0, 4, 3, 2), createLobbySwitchItem("Lobby 1", "Klicke um Lobby 1 zu betreten."), switchLobby(0));
    setupPane(createStaticPane(3, 4, 3, 2), createLobbySwitchItem("Lobby 2", "Klicke um Lobby 2 zu betreten."), switchLobby(1));
    setupPane(createStaticPane(6, 4, 3, 2), createLobbySwitchItem("Lobby 3", "Klicke um Lobby 3 zu betreten."), switchLobby(2));
    // @formatter:on
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

  private ItemStack createEventServerSwitchItem() {
    final ItemStack item = createInvisibleItem();

    item.editMeta(itemMeta -> {
      itemMeta.displayName(createNonItalicComponent("Event Server", Colors.PRIMARY));
      itemMeta.lore(createNonItalicLore(
          SettingManager.isEventServerEnabled()
              ? text("Klicke um den Server beizutreten.")
              : text("Der Event Server ist derzeit deaktiviert.", Colors.ERROR)
      ));
    });

    return item;
  }

  private ItemStack createCommunityServerSwitchItem() {
    final ItemStack item = createInvisibleItem();

    item.editMeta(itemMeta -> {
      itemMeta.displayName(createNonItalicComponent("Community Server", Colors.PRIMARY));
      itemMeta.lore(createNonItalicLore(
          text("Aktuell ist der Community Server nur", Colors.ERROR),
          text("über ", Colors.ERROR)
              .append(text("server.castcrafter.de", Colors.VARIABLE_VALUE))
              .append(text(" erreichbar.", Colors.ERROR))
      ));
    });

    return item;
  }

  private ItemStack createLobbySwitchItem(String displayName, String lore) {
    final ItemStack item = createInvisibleItem();

    item.editMeta(itemMeta -> {
      itemMeta.displayName(createNonItalicComponent(displayName, Colors.PRIMARY));
      itemMeta.lore(createNonItalicLore(text(lore)));
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

  @Contract(pure = true)
  private @NotNull Consumer<InventoryClickEvent> switchEventServer() {
    return toPlayer(player -> {
      if (!SettingManager.isEventServerEnabled()) {
        Messages.EVENT_SERVER_DISABLED.send(player);
        return;
      }
      SurfServerSelectorApi.getPlayer(player.getUniqueId())
          .changeServer(SettingManager.getCurrentEventServer(), true);
    });
  }

  @Contract(pure = true)
  private @NotNull Consumer<InventoryClickEvent> switchCommunityServer() {
    return toPlayer(player -> {
//      SurfServerSelectorApi.getPlayer(player.getUniqueId())
//          .changeServer(SettingManager.getCommunityServer(), true);

      Messages.COMMUNITY_SERVER_NOT_AVAILABLE.send(player, text("server.castcrafter.de"));
    });
  }

  @Contract(pure = true)
  private @NotNull Consumer<InventoryClickEvent> switchLobby(int lobbyIndex) {
    return toPlayer(player -> {
      @Nullable String lobbyServerName = ListUtil.getOrNull(SettingManager.getLobbyServerNames(),
          lobbyIndex);
      if (lobbyServerName == null) {
        Messages.LOBBY_SERVER_NOT_AVAILABLE.send(player, text(lobbyIndex + 1));
        return;
      }
      SurfServerSelectorApi.getPlayer(player.getUniqueId())
          .changeServer(lobbyServerName, true, false);
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
