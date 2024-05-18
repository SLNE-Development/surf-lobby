package dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item.switcher;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.bukkit.common.settings.SettingManager;
import dev.slne.surf.surfserverselector.bukkit.lobby.BukkitMain;
import dev.slne.surf.surfserverselector.core.message.Messages;
import dev.slne.surf.surfserverselector.core.util.ListUtil;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LobbySwitcherGui extends ChestGui {

  public LobbySwitcherGui() {
    super(6, "<shift:-8><glyph:server_selector>", BukkitMain.getInstance());

    setOnGlobalClick(cancel());
    setOnGlobalDrag(cancel());

    final StaticPane eventServerSwitchPane = new StaticPane(1, 0, 3, 3);
    final StaticPane communityServerSwitchPane = new StaticPane(5, 0, 3, 3);
    final StaticPane lobby1SwitchPane = new StaticPane(0, 4, 3, 2);
    final StaticPane lobby2SwitchPane = new StaticPane(3, 4, 3, 2);
    final StaticPane lobby3SwitchPane = new StaticPane(6, 4, 3, 2);

    eventServerSwitchPane.setOnClick(switchEventServer());
    communityServerSwitchPane.setOnClick(switchCommunityServer());

    lobby1SwitchPane.setOnClick(switchLobby(0));
    lobby2SwitchPane.setOnClick(switchLobby(1));
    lobby3SwitchPane.setOnClick(switchLobby(2));

    addPane(eventServerSwitchPane);
    addPane(communityServerSwitchPane);
    addPane(lobby1SwitchPane);
    addPane(lobby2SwitchPane);
    addPane(lobby3SwitchPane);
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
      SurfServerSelectorApi.getPlayer(player.getUniqueId())
          .changeServer(SettingManager.getCommunityServer(), true);
    });
  }

  @Contract(pure = true)
  private @NotNull Consumer<InventoryClickEvent> switchLobby(int lobbyIndex) {
    return toPlayer(player -> {
      final @Nullable String lobbyServerName = ListUtil.getOrNull(
          SettingManager.getLobbyServerNames(), lobbyIndex);

      if (lobbyServerName == null) {
        Messages.LOBBY_SERVER_NOT_AVAILABLE.send(player, Component.text(lobbyIndex + 1));
        return;
      }

      SurfServerSelectorApi.getPlayer(player.getUniqueId())
          .changeServer(lobbyServerName, true, false);
    });
  }

  @Contract(pure = true)
  private @NotNull Consumer<InventoryClickEvent> toPlayer(Consumer<Player> consumer) {
    return event -> {
      if (!(event.getWhoClicked() instanceof Player player)) {
        return;
      }
      player.closeInventory();

      consumer.accept(player);
    };
  }

  @Contract(pure = true)
  private <T extends Cancellable> @NotNull Consumer<T> cancel() {
    return event -> event.setCancelled(true);
  }
}
