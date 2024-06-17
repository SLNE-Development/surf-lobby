package dev.slne.surf.lobby.bukkit.lobby.jnr.config;

import dev.slne.surf.surfapi.bukkit.api.SurfBukkitApi;
import io.papermc.paper.math.BlockPosition;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class JNRConfig {

  public BlockPosition startPosition = null;
  public BlockPosition leaderboardPosition = null;

  public int maxDistanceBetweenBlocks = 5;
  public int maxDistanceBetweenBlocksY = 1;
  public int jumpsToWin = 30;

  public static JNRConfig get() {
    return SurfBukkitApi.get().getModernConfig(JNRConfig.class);
  }
}
