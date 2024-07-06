package dev.slne.surf.lobby.bukkit.lobby.jnr.jump.jumps;

import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.Jump;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.JumpBlock;
import java.util.List;
import org.bukkit.Material;

/**
 * The type Jump 3 wide.
 */
public class Jump3Wide extends Jump {

  public static final Jump3Wide INSTANCE = new Jump3Wide();

  @Override
  public List<JumpBlock> populateBlocks() {
    return List.of(new JumpBlock(Material.WHITE_WOOL, 4, 0, 0));
  }
}
