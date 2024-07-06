package dev.slne.surf.lobby.bukkit.lobby.jnr;


import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.JumpRegistry;
import java.security.SecureRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * The type Jnr manager.
 */
public class JnrManager {

  public static final JnrManager INSTANCE = new JnrManager();

  private final Location testLocation;

  private final SecureRandom random;
  private final JumpRegistry jumpRegistry = JumpRegistry.INSTANCE;

  /**
   * Instantiates a new Jnr manager.
   */
  public JnrManager() {
    this.random = new SecureRandom();
    this.testLocation = new Location(Bukkit.getWorld("lobby"), 0, 80, -260);
  }

  /**
   * Generate jnr.
   */
  public void generateJnr() {
    JNR.INSTANCE.addBlocksToWorld();
  }

  /**
   * Gets test location.
   *
   * @return the test location
   */
  public Location getTestLocation() {
    return testLocation;
  }

  /**
   * Gets jump registry.
   *
   * @return the jump registry
   */
  public JumpRegistry getJumpRegistry() {
    return jumpRegistry;
  }

  /**
   * Gets random.
   *
   * @return the random
   */
  public SecureRandom getRandom() {
    return random;
  }
}
