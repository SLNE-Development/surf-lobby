package dev.slne.surf.surfserverselector.bukkit.lobby.listener.qole;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAttackListener implements Listener {

  @EventHandler
  public void onPlayerAttack(PrePlayerAttackEntityEvent event) {
    event.setCancelled(true);
  }

}
