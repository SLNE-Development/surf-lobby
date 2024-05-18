package dev.slne.surf.surfserverselector.bukkit.lobby.listener.hotbar;

import dev.slne.surf.surfserverselector.bukkit.lobby.hotbar.PlayerHotbar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public final class DestroyPlayerHotbarListener implements Listener {

  @EventHandler
  public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
    PlayerHotbar.destroyHotbar(event.getPlayer());
  }
}
