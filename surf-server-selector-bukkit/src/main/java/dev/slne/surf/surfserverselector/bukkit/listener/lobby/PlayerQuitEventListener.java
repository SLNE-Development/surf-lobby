package dev.slne.surf.surfserverselector.bukkit.listener.lobby;

import dev.slne.surf.surfserverselector.bukkit.hotbar.PlayerHotbar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerQuitEventListener implements Listener {

  @EventHandler
  public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
    PlayerHotbar.destroyHotbar(event.getPlayer());
  }
}
