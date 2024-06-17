package dev.slne.surf.lobby.bukkit.lobby.hotbar.item.switcher;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.bukkit.common.settings.SettingManager;
import dev.slne.surf.lobby.bukkit.lobby.BukkitMain;
import dev.slne.surf.lobby.core.message.Messages;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.EventServerData;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.data.LobbyServerData;
import dev.slne.surf.lobby.core.util.ListUtil;
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
import org.jetbrains.annotations.Nullable;

public final class LobbySwitcherGui extends ChestGui {

  private static final String INVISIBLE_ITEM_ID = "invisible_item";
  private final BukkitMain plugin = BukkitMain.getInstance();

  public LobbySwitcherGui() {
    super(6, "<shift:-46><glyph:server_selector>", BukkitMain.getInstance());

    setOnGlobalClick(cancel());
    setOnGlobalDrag(cancel());

    // @formatter:off
    setupPane(createStaticPane(5, 0, 3, 3), createEventServerSwitchItem(), switchEventServer());
    setupPane(createStaticPane(1, 0, 3, 3), createCommunityServerSwitchItem(), switchCommunityServer());
    setupPane(createStaticPane(0, 4, 3, 2), createLobbySwitchItem(0), switchLobby(0));
    setupPane(createStaticPane(3, 4, 3, 2), createLobbySwitchItem(1), switchLobby(1));
    setupPane(createStaticPane(6, 4, 3, 2), createLobbySwitchItem(2), switchLobby(2));
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
      final EventServerData eventServerData = SettingManager.getEventServerData();

      itemMeta.displayName(createNonItalicComponent("Event Server", Colors.PRIMARY));
      itemMeta.lore(createNonItalicLore(
          createPlayersOnlineLore(eventServerData.getOnlinePlayers(), eventServerData.getMaxPlayers()),
          empty(),
          eventServerData.isEventServerEnabled()
              ? text("Klicke um den Server beizutreten.")
              : text("Der Event Server ist derzeit deaktiviert.", Colors.ERROR)
      ));
    });

    return item;
  }

  private ItemStack createCommunityServerSwitchItem() {
    final ItemStack item = createInvisibleItem();

    item.editMeta(itemMeta -> {
//      final CommunityServerData communityServerData = SettingManager.getCommunityServerData();

      itemMeta.displayName(createNonItalicComponent("Survival Server", Colors.PRIMARY));
      itemMeta.lore(createNonItalicLore(
          text("Der Server ist aktuell geschlossen!", Colors.ERROR),
          text("Für Informationen zum Start der ", Colors.ERROR)
              .append(text("1.21 Season", Colors.VARIABLE_VALUE))
              .append(text(" besuche unseren Discord.", Colors.ERROR))
      ));
    });

    return item;
  }

  private @NotNull ItemStack createLobbySwitchItem(int lobbyIndex) {
    final ItemStack item = createInvisibleItem();

    item.editMeta(itemMeta -> {
      final String displayName = "Lobby %d".formatted(lobbyIndex+1);

      itemMeta.displayName(createNonItalicComponent(displayName, Colors.PRIMARY));
      itemMeta.lore(createNonItalicLore(
          createLobbyOnlinePlayersLore(lobbyIndex),
          empty(),
          text("Klicke um %s zu betreten.".formatted(displayName))
      ));
    });

    return item;
  }

  private @NotNull Component createLobbyOnlinePlayersLore(int lobbyIndex) {
    int playersOnline, maxPlayers;

    try {
      final LobbyServerData lobbyServerData = SettingManager.getLobbyServerData().getValue(lobbyIndex);

      playersOnline = lobbyServerData.getOnlinePlayers();
      maxPlayers = lobbyServerData.getMaxPlayers();
    } catch (IndexOutOfBoundsException e) {
      playersOnline = -1;
      maxPlayers = -1;
    }

    return createPlayersOnlineLore(playersOnline, maxPlayers);
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
  private @NotNull Consumer<InventoryClickEvent> switchEventServer() {
    return toPlayer(player -> {
      if (!SettingManager.getEventServerData().isEventServerEnabled()) {
        Messages.EVENT_SERVER_DISABLED.send(player);
        return;
      }
      LobbyApi.getPlayer(player.getUniqueId())
          .changeServer(SettingManager.getEventServerData().getEventServerName(), true);
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
      @Nullable String lobbyServerName = ListUtil.getOrNull(
          SettingManager.getLobbyServerData().keyList(), lobbyIndex);
      if (lobbyServerName == null) {
        Messages.LOBBY_SERVER_NOT_AVAILABLE.send(player, text(lobbyIndex + 1));
        return;
      }
      LobbyApi.getPlayer(player.getUniqueId())
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
