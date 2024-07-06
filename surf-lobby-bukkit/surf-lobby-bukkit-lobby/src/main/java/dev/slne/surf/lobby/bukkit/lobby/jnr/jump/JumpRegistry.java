package dev.slne.surf.lobby.bukkit.lobby.jnr.jump;

import dev.slne.surf.lobby.bukkit.lobby.jnr.JnrManager;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.jumps.Jump3Wide;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.jumps.Jump3Wide1Down;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.jumps.Jump3Wide1Up;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.jumps.Jump4Wide;
import dev.slne.surf.lobby.bukkit.lobby.jnr.jump.jumps.Jump4Wide1Down;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Jump registry.
 */
public class JumpRegistry {

  public static final JumpRegistry INSTANCE = new JumpRegistry();

  private final List<Jump> jumps;

  /**
   * Instantiates a new Jump registry.
   */
  public JumpRegistry() {
    this.jumps = new ArrayList<>();

    this.jumps.add(Jump3Wide.INSTANCE);
    this.jumps.add(Jump3Wide1Up.INSTANCE);
    this.jumps.add(Jump3Wide1Down.INSTANCE);
    this.jumps.add(Jump4Wide.INSTANCE);
    this.jumps.add(Jump4Wide1Down.INSTANCE);
  }

  /**
   * Gets random jump.
   *
   * @param newDirection the new direction
   * @return the random jump
   */
  public Jump getRandomJump(JumpDirection newDirection) {
    return jumps.get(JnrManager.INSTANCE.getRandom().nextInt(jumps.size())).rotateBlocks(newDirection);
  }

  /**
   * Gets jumps.
   *
   * @return the jumps
   */
  public List<Jump> getJumps() {
    return jumps;
  }
}
