package dev.slne.surf.lobby.bukkit.lobby.listener.features;

import dev.slne.surf.lobby.core.permissions.Permissions;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerPushbackAttackListener implements Listener {

  private static final double PUSHBACK_FORCE = 2;

  @EventHandler(ignoreCancelled = true)
  public void onAttack(PrePlayerAttackEntityEvent event) {
    Player player = event.getPlayer();
    Entity entity = event.getAttacked();

    if(!(entity instanceof Player attacked)) {
      return;
    }

    if(!player.isSneaking()) {
      return;
    }

    if(!player.hasPermission(Permissions.FEATURE_USE_PLAYER_PUSHBACK_ATTACK.getPermission())) {
      return;
    }

    attacked.setVelocity(player.getEyeLocation().getDirection().multiply(PUSHBACK_FORCE));
  }

}
