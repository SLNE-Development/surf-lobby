package dev.slne.surf.lobby.bukkit.lobby.listener.qol;

import dev.slne.surf.lobby.core.permissions.Permissions;
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
