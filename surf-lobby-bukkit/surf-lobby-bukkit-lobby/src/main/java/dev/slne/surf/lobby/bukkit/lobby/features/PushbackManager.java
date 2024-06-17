package dev.slne.surf.lobby.bukkit.lobby.features;

import dev.slne.surf.lobby.bukkit.lobby.scheduler.LobbySchedulerChild;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * The type Pushback manager.
 */
public class PushbackManager implements LobbySchedulerChild, Listener {

  public static final PushbackManager INSTANCE = new PushbackManager();

  private static final int RANGE = 3;

  private static final double FORCE = -0.5;
  private static final double Y_FORCE = 0.5;

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    removePlayer(event.getPlayer());
  }

  @Override
  public void tickScheduler() {
    for (Player player : pushingBack) {
      for(Entity closeEntity : player.getNearbyEntities(RANGE, RANGE, RANGE)) {
        if(closeEntity.equals(player)) {
          continue;
        }

        if(!(closeEntity instanceof Player closePlayer)) {
          continue;
        }

        // throw closePlayer away
//        if(closePlayer.hasPermission(Permissions.FEATURE_USE_PLAYER_PUSHBACK_ITEM)) {
//          continue;
//        }

        closePlayer.setVelocity(player.getLocation().toVector().subtract(closePlayer.getLocation().toVector()).multiply(FORCE).setY(Y_FORCE));
      }

      player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, null);
    }
  }

  private final Set<Player> pushingBack;

  /**
   * Instantiates a new Pushback manager.
   */
  private PushbackManager() {
    this.pushingBack = new HashSet<>();
  }

  /**
   * Add player.
   *
   * @param player the player
   */
  public void addPlayer(Player player) {
    pushingBack.add(player);
  }

  /**
   * Remove player.
   *
   * @param player the player
   */
  public void removePlayer(Player player) {
    pushingBack.remove(player);
  }

  /**
   * Toggle player pushback.
   *
   * @param player  the player
   * @param enabled the enabled
   */
  public void togglePlayerPushback(Player player, boolean enabled) {
    if(enabled) {
      addPlayer(player);
    } else {
      removePlayer(player);
    }
  }
}
