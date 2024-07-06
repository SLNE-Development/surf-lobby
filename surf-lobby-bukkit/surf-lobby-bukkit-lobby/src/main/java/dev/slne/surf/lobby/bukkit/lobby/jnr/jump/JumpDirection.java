package dev.slne.surf.lobby.bukkit.lobby.jnr.jump;

/**
 * The enum Jump direction.
 */
public enum JumpDirection {

  NORTH(0, -1),
  EAST(1, 0),
  SOUTH(0, 1),
  WEST(-1, 0);

  private final int x;
  private final int z;

  /**
   * Instantiates a new Jump direction.
   *
   * @param x the x
   * @param z the z
   */
  JumpDirection(int x, int z) {
    this.x = x;
    this.z = z;
  }

  /**
   * Gets x.
   *
   * @return the x
   */
  public int getX() {
    return x;
  }

  /**
   * Gets z.
   *
   * @return the z
   */
  public int getZ() {
    return z;
  }

}
