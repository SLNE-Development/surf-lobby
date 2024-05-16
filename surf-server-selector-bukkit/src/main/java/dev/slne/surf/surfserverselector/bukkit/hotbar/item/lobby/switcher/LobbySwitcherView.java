package dev.slne.surf.surfserverselector.bukkit.hotbar.item.lobby.switcher;

import dev.slne.surf.surfapi.core.api.messages.Colors;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.bukkit.settings.SettingManager;
import dev.slne.surf.surfserverselector.bukkit.util.ItemStackBuilder;
import dev.slne.surf.surfserverselector.core.message.Messages;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbySwitcherView extends View {

  private static final char EVENT_SERVER_SWITCH = 'e', COMMUNITY_SERVER_SWITCH = 'c', LOBBY_SERVER_SWITCH = 'l';

  @Override
  public void onInit(@NotNull ViewConfigBuilder config) {
    config
        .title(Component.text("Lobby Switcher", Colors.PRIMARY))
        .cancelDefaults()
        .type(ViewType.CHEST);
//        .layout(
//            "         ",
//            "  %s  %s   ".formatted(EVENT_SERVER_SWITCH, COMMUNITY_SERVER_SWITCH),
//            "         ",
//            "  %s %s %s  ".formatted(LOBBY_SERVER_SWITCH, LOBBY_SERVER_SWITCH, LOBBY_SERVER_SWITCH),
//            "         ",
//            "         "
//        );
  }

  @Override
  public void onFirstRender(@NotNull RenderContext render) {

    System.out.println("First render");

    render.layoutSlot(EVENT_SERVER_SWITCH)
        .withItem(ItemStackBuilder.create(Material.COMPASS)
            .withDisplayName(Component.text("Event Server", Colors.PRIMARY))
            .withLore(
                Component.text("Click to join the event server", Colors.SECONDARY)
            )
            .build())
        .onClick(context -> {
          context.closeForPlayer();
          final Player clicker = context.getPlayer();

          if (!SettingManager.isEventServerEnabled()) {
            Messages.EVENT_SERVER_DISABLED.send(clicker);
            return;
          }

          SurfServerSelectorApi.getPlayer(clicker.getUniqueId())
              .changeServer(SettingManager.getCurrentEventServer(), true);
        });

    render.layoutSlot(COMMUNITY_SERVER_SWITCH)
        .withItem(ItemStackBuilder.create(Material.GRASS_BLOCK)
            .withDisplayName(Component.text("Community Server", Colors.PRIMARY))
            .withLore(
                Component.text("Click to join the community server", Colors.SECONDARY)
            )
            .build())
        .onClick(context -> {
          context.closeForPlayer();
          final Player clicker = context.getPlayer();

          SurfServerSelectorApi.getPlayer(clicker.getUniqueId())
              .changeServer(SettingManager.getCommunityServer(), true);
        });
  }

  @Override
  public void onOpen(@NotNull OpenContext open) {
    System.out.println("Opened");
  }
}

