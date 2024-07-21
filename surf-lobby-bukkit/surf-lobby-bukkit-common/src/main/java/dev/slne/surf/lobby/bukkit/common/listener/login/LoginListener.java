package dev.slne.surf.lobby.bukkit.common.listener.login;

import dev.slne.surf.lobby.core.permissions.Permissions;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

public final class LoginListener implements Listener {

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerLogin(@NotNull PlayerLoginEvent event) {
    Player player = event.getPlayer();
    Server server = player.getServer();

    boolean whitelistEnabled = server.hasWhitelist();
    boolean isWhitelisted = player.isWhitelisted();
    boolean playerLimitReached = server.getOnlinePlayers().size() >= server.getMaxPlayers();

    if(!playerLimitReached) {
      return;
    }

    if(whitelistEnabled && !isWhitelisted) {
      return;
    }

    if (player.hasPermission(Permissions.BYPASS_PLAYER_LIMIT_PERMISSION.getPermission())) {
      event.allow();
    }
  }
}
