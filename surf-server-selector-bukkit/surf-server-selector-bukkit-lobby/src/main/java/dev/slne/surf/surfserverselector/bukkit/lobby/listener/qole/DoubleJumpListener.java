package dev.slne.surf.surfserverselector.bukkit.lobby.listener.qole;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

public final class DoubleJumpListener implements Listener {

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    final Player player = event.getPlayer();

    if (player.getGameMode() != GameMode.CREATIVE
        && player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR
        && !player.isFlying()) {
      player.setAllowFlight(true);
    }
  }

  @EventHandler
  public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
    final Player player = event.getPlayer();

    if (player.getGameMode().equals(GameMode.CREATIVE)) {
      return;
    }

    final Vector direction = player.getEyeLocation().getDirection();
    final Location loc = player.getLocation();

    player.setVelocity(direction.multiply(2).setY(1));
    loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 10);

    player.setAllowFlight(false);
    player.setFlying(false);

    event.setCancelled(true);
  }
}
