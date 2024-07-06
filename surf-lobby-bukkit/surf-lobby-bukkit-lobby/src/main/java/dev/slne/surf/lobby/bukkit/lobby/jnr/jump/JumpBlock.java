package dev.slne.surf.lobby.bukkit.lobby.jnr.jump;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * The type Jump block.
 */
@SuppressWarnings("FieldMayBeFinal")
public class JumpBlock {

  private Material material;
  private int x;
  private int y;
  private int z;

  /**
   * Instantiates a new Jump block.
   *
   * @param material the material
   * @param x        the x
   * @param y        the y
   * @param z        the z
   */
  public JumpBlock(Material material, int x, int y, int z) {
    this.material = material;
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Gets material.
   *
   * @return the material
   */
  public Material getMaterial() {
    return material;
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
   * Gets y.
   *
   * @return the y
   */
  public int getY() {
    return y;
  }

  /**
   * Gets z.
   *
   * @return the z
   */
  public int getZ() {
    return z;
  }

  /**
   * Sets material.
   *
   * @param material the material
   */
  public void setMaterial(Material material) {
    this.material = material;
  }

  /**
   * Sets x.
   *
   * @param x the x
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Sets y.
   *
   * @param y the y
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Sets z.
   *
   * @param z the z
   */
  public void setZ(int z) {
    this.z = z;
  }

  /**
   * Add block to world.
   *
   * @param location the location
   */
  public void addBlockToWorld(Location location) {
    location.getWorld().getBlockAt(location.clone().add(x, y, z)).setType(material);
  }

  /**
   * Remove block from world.
   *
   * @param location the location
   */
  public void removeBlockFromWorld(Location location) {
    location.getWorld().getBlockAt(location.clone().add(x, y, z)).setType(Material.AIR);
  }
}
