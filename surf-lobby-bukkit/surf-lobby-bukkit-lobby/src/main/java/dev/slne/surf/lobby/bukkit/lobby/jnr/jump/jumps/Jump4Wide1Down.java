package dev.slne.surf.lobby.bukkit.lobby.jnr.jump.jumps;

import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.Jump;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.JumpBlock;
import java.util.List;
import org.bukkit.Material;

/**
 * The type Jump 3 wide.
 */
public class Jump4Wide1Down extends Jump {

  public static final Jump4Wide1Down INSTANCE = new Jump4Wide1Down();

  @Override
  public List<JumpBlock> populateBlocks() {
    return List.of(new JumpBlock(Material.WHITE_WOOL, 5, -1, 0));
  }
}
