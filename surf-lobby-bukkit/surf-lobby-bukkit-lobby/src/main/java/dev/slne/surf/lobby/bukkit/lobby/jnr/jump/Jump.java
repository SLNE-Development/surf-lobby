package dev.slne.surf.lobby.bukkit.lobby.jnr.jump;

import java.util.List;
import org.bukkit.Location;

/**
 * The type Jump.
 */
public abstract class Jump {

  private List<JumpBlock> blocks;

  /**
   * Gets blocks.
   *
   * @return the blocks
   */
  public abstract List<JumpBlock> populateBlocks();

  /**
   * Add blocks to world.
   *
   * @param pasteAt the paste at
   * @return the location
   */
  public Location addBlocksToWorld(Location pasteAt) {
    if(blocks == null) {
      blocks = populateBlocks();
    }

    Location endLocation = pasteAt.clone();
    for(JumpBlock block : blocks) {
      block.addBlockToWorld(pasteAt);

      int currentX = endLocation.getBlockX();
      int currentY = endLocation.getBlockY();
      int currentZ = endLocation.getBlockZ();

      endLocation.setX(currentX + block.getX());
      endLocation.setY(currentY + block.getY());
      endLocation.setZ(currentZ + block.getZ());
    }

    return endLocation;
  }

  /**
   * Remove blocks from world.
   *
   * @param pasteAt the paste at
   */
  public void removeBlocksFromWorld(Location pasteAt) {
    if(blocks == null) {
      blocks = populateBlocks();
    }

    for(JumpBlock block : blocks) {
      block.removeBlockFromWorld(pasteAt);
    }
  }

  /**
   * Rotate blocks.
   *
   * @param newDirection the new direction
   * @return the jump
   */
  public Jump rotateBlocks(JumpDirection newDirection) {
    if(blocks == null) {
      blocks = populateBlocks();
    }

    for(JumpBlock block : blocks) {
      int currentX = block.getX();
      int currentZ = block.getZ();

      int directionX = newDirection.getX();
      int directionZ = newDirection.getZ();

      block.setX(currentX * directionX + currentZ * directionZ);
      block.setZ(currentX * directionZ + currentZ * directionX);
    }

    return this;
  }

}
