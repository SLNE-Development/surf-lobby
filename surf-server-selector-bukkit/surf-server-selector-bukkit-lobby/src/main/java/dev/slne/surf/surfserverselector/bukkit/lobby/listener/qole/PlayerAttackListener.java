package dev.slne.surf.surfserverselector.bukkit.lobby.listener.qole;

import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAttackListener implements Listener {

  @EventHandler
  public void onPlayerAttack(PrePlayerAttackEntityEvent event) {
    if (event.getPlayer().hasPermission(Permissions.BYPASS_HUB_ATTACK_PERMISSION.getPermission())) {
      return;
    }
    event.setCancelled(true);
  }

}
