package dev.slne.surf.lobby.bukkit.lobby.scheduler;

import dev.slne.surf.lobby.bukkit.lobby.BukkitMain;
import dev.slne.surf.lobby.bukkit.lobby.features.PushbackManager;
import java.util.HashSet;
import java.util.Set;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * The type Lobby scheduler.
 */
public class LobbyScheduler extends BukkitRunnable {

  private static final ComponentLogger LOGGER = ComponentLogger.logger("LobbyScheduler");
  private final Set<LobbySchedulerChild> children;

  /**
   * Instantiates a new Lobby scheduler.
   */
  public LobbyScheduler() {
    this.children = new HashSet<>();

    this.children.add(PushbackManager.INSTANCE);
  }

  /**
   * Start.
   */
  public void start() {
    this.runTaskTimer(BukkitMain.getInstance(), 0L, 10L);
  }

  /**
   * Stop.
   */
  public void stop() {
    try {
      if(!isCancelled()) {
        cancel();
      }
    } catch (Exception exception) {
      if(exception instanceof IllegalStateException) {
        // IGNORE
        return;
      }

      LOGGER.error("Failed to stop scheduler", exception);
    }
  }

  @Override
  public void run() {
    for (LobbySchedulerChild child : children) {
      child.tickScheduler();
    }
  }
}
