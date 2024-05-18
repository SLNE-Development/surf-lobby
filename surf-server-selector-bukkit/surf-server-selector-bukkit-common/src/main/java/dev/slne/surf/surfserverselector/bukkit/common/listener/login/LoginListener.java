package dev.slne.surf.surfserverselector.bukkit.common.listener.login;

import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

public final class LoginListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerLogin(@NotNull PlayerLoginEvent event) {
    if (event.getPlayer().hasPermission(Permissions.BYPASS_PLAYER_LIMIT_PERMISSION.getPermission())) {
      event.allow();
    }
  }
}
