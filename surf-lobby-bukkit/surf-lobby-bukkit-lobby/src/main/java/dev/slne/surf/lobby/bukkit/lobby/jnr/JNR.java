package dev.slne.surf.lobby.bukkit.lobby.jnr;

import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.Jump;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.JumpDirection;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.JumpRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

/**
 * The type Jnr.
 */
public class JNR {

  public static final JNR INSTANCE = new JNR();
  public static final int GENERATE_JUMP_AMOUNT = 5;

  private final List<Jump> jumpBlocks;
  private JumpDirection lastDirection;

  /**
   * Instantiates a new Jnr.
   */
  public JNR() {
    this.jumpBlocks = new ArrayList<>();

    for(int i = 0; i < GENERATE_JUMP_AMOUNT; i++) {
      JumpDirection randomLocation = getValidRandomLocation();

      jumpBlocks.add(JumpRegistry.INSTANCE.getRandomJump(randomLocation));

      lastDirection = randomLocation;
    }
  }

  private JumpDirection getValidRandomLocation(){
    List<JumpDirection> validDirections = Arrays.stream(JumpDirection.values())
      .filter(direction -> direction != lastDirection)
      .toList();

    return validDirections.get(JnrManager.INSTANCE.getRandom().nextInt(validDirections.size()));
  }

  /**
   * Add blocks to world.
   *
   * @return the location
   */
  public Location addBlocksToWorld() {
    Location location = JnrManager.INSTANCE.getTestLocation();

    for(Jump jump : jumpBlocks) {
      location = jump.addBlocksToWorld(location);
    }

    return location;
  }

  /**
   * Remove blocks from world.
   */
  public void removeBlocksFromWorld() {
    for(Jump jump : jumpBlocks) {
      jump.removeBlocksFromWorld(JnrManager.INSTANCE.getTestLocation());
    }
  }

  /**
   * Gets jump blocks.
   *
   * @return the jump blocks
   */
  public List<Jump> getJumpBlocks() {
    return jumpBlocks;
  }
}
