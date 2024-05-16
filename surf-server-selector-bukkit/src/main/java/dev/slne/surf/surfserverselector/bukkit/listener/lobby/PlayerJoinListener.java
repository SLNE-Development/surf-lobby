package dev.slne.surf.surfserverselector.bukkit.listener.lobby;

import dev.slne.surf.surfserverselector.bukkit.hotbar.PlayerHotbar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerJoinListener implements Listener {

  @EventHandler
  public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
    final Player player = event.getPlayer();
    final PlayerHotbar playerHotbar = new PlayerHotbar(player);

    playerHotbar.populateHotbar(); // TODO: 15.05.2024 22:13 - delay?
  }
}
