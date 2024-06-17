package dev.slne.surf.lobby.bukkit.lobby.listener.hotbar;

import dev.slne.surf.lobby.bukkit.lobby.hotbar.PlayerHotbar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public final class PopulatePlayerHotbarListener implements Listener {

  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    final PlayerHotbar playerHotbar = new PlayerHotbar(player);

    playerHotbar.populateHotbar();
  }
}
