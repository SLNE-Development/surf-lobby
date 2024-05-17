package dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.item.switcher;

import dev.slne.surf.surfapi.core.api.messages.Colors;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.bukkit.common.settings.SettingManager;
import dev.slne.surf.surfserverselector.bukkit.common.util.ItemStackBuilder;
import dev.slne.surf.surfserverselector.core.message.Messages;
import dev.slne.surf.surfserverselector.core.util.ListUtil;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LobbySwitcherView extends View {

  @Override
  public void onInit(@NotNull ViewConfigBuilder config) {
    config
        .title(Component.text("Lobby Switcher", Colors.PRIMARY))
        .cancelOnClick().cancelOnDrag().cancelOnPickup().cancelOnDrop()
        .type(ViewType.CHEST)
        .size(54);
  }

  @Override
  public void onFirstRender(@NotNull RenderContext render) {
    eventServerSwitchItem(render, 1, 2);
    eventServerSwitchItem(render, 1, 3);
    eventServerSwitchItem(render, 1, 4);
    eventServerSwitchItem(render, 2, 2);
    eventServerSwitchItem(render, 2, 3);
    eventServerSwitchItem(render, 2, 4);
    eventServerSwitchItem(render, 3, 2);
    eventServerSwitchItem(render, 3, 3);
    eventServerSwitchItem(render, 3, 4);

    communityServerSwitch(render, 1, 6);
    communityServerSwitch(render, 1, 7);
    communityServerSwitch(render, 1, 8);
    communityServerSwitch(render, 2, 6);
    communityServerSwitch(render, 2, 7);
    communityServerSwitch(render, 2, 8);
    communityServerSwitch(render, 3, 6);
    communityServerSwitch(render, 3, 7);
    communityServerSwitch(render, 3, 8);

    // Lobby 1
    lobbySwitches(render, 5, 1, 0);
    lobbySwitches(render, 5, 2, 0);
    lobbySwitches(render, 5, 3, 0);
    lobbySwitches(render, 6, 1, 0);
    lobbySwitches(render, 6, 2, 0);
    lobbySwitches(render, 6, 3, 0);

    // Lobby 2
    lobbySwitches(render, 5, 4, 1);
    lobbySwitches(render, 5, 5, 1);
    lobbySwitches(render, 5, 6, 1);
    lobbySwitches(render, 6, 4, 1);
    lobbySwitches(render, 6, 5, 1);
    lobbySwitches(render, 6, 6, 1);

    // Lobby 3
    lobbySwitches(render, 5, 7, 2);
    lobbySwitches(render, 5, 8, 2);
    lobbySwitches(render, 5, 9, 2);
    lobbySwitches(render, 6, 7, 2);
    lobbySwitches(render, 6, 8, 2);
    lobbySwitches(render, 6, 9, 2);
  }

  @Override
  public void onOpen(@NotNull OpenContext open) {
    System.out.println("Opened");
  }

  private void eventServerSwitchItem(RenderContext render, int row, int slot) {
    render.slot(row, slot)
        .withItem(ItemStackBuilder.create(Material.COMPASS)
            .withDisplayName(Component.text("Event Server", Colors.PRIMARY))
            .withLore(
                Component.text("Click to join the event server", Colors.SECONDARY)
            )
            .build())
        .onClick(context -> {
          final Player clicker = context.getPlayer();
          clicker.closeInventory();

          if (!SettingManager.isEventServerEnabled()) {
            Messages.EVENT_SERVER_DISABLED.send(clicker);
            return;
          }

          SurfServerSelectorApi.getPlayer(clicker.getUniqueId())
              .changeServer(SettingManager.getCurrentEventServer(), true);
        });
  }

  private void communityServerSwitch(RenderContext render, int row, int slot) {
    render.slot(row, slot)
        .withItem(ItemStackBuilder.create(Material.GRASS_BLOCK)
            .withDisplayName(Component.text("Community Server", Colors.PRIMARY))
            .withLore(
                Component.text("Click to join the community server", Colors.SECONDARY)
            )
            .build())
        .onClick(context -> {
          final Player clicker = context.getPlayer();
          clicker.closeInventory();

          SurfServerSelectorApi.getPlayer(clicker.getUniqueId())
              .changeServer(SettingManager.getCommunityServer(), true);
        });
  }

  private void lobbySwitches(RenderContext render, int row, int slot, int lobbyIndex) {
    final @Nullable String lobbyServerName = ListUtil.getOrNull(
        SettingManager.getLobbyServerNames(), lobbyIndex);

    render.slot(row, slot)
        .withItem(ItemStackBuilder.create(Material.ACACIA_LEAVES)
            .withDisplayName(Component.text("Lobby " + lobbyIndex + 1))
            .withLore(Component.text("Click to switch lobby"))
            .build())
        .onClick(context -> {
          final Player clicker = context.getPlayer();
          clicker.closeInventory();

          if (lobbyServerName == null) {
            Messages.LOBBY_SERVER_NOT_AVAILABLE.send(clicker, Component.text(lobbyIndex + 1));
            return;
          }

          SurfServerSelectorApi.getPlayer(clicker.getUniqueId())
              .changeServer(lobbyServerName, true, false);
        });

  }
}

