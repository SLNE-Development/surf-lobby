package dev.slne.surf.lobby.bukkit.lobby.jnr.jump.jumps;

import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.Jump;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.JumpBlock;
import java.util.List;
import org.bukkit.Material;

/**
 * The type Jump 3 wide.
 */
public class Jump4Wide extends Jump {

  public static final Jump4Wide INSTANCE = new Jump4Wide();

  @Override
  public List<JumpBlock> populateBlocks() {
    return List.of(new JumpBlock(Material.WHITE_WOOL, 5, 0, 0));
  }
}
