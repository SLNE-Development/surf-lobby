package dev.slne.surf.lobby.bukkit.lobby.jnr.game.location;

import dev.slne.surf.lobby.bukkit.lobby.jnr.config.JNRConfig;
import dev.slne.surf.surfserverselector.bukkit.lobby.jnr.random.JNRRandomAccessor;
import java.security.SecureRandom;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class LocationGenerator {
  public static final LocationGenerator INSTANCE = new LocationGenerator();

  private LocationGenerator() {
  }

  public Location generateNextSafeJNRLocation(Player player) {
    final JNRConfig config = JNRConfig.get();
    final SecureRandom random = JNRRandomAccessor.getRandom();
    final Location nextLocation = player.getLocation().clone();

    final int maxDistanceBetweenBlocks = config.maxDistanceBetweenBlocks;
    final int maxDistanceBetweenBlocksY = config.maxDistanceBetweenBlocksY;

    final int addX = random.nextInt(-maxDistanceBetweenBlocks, maxDistanceBetweenBlocks);
    final int addY = random.nextInt(-maxDistanceBetweenBlocksY, maxDistanceBetweenBlocksY);
    final int addZ = random.nextInt(-maxDistanceBetweenBlocks, maxDistanceBetweenBlocks);

    nextLocation.add(addX, addY, addZ);

    if (nextLocation.getBlock().getType().isAir() && !player.hasLineOfSight(nextLocation)) {
      return nextLocation;
    } else {
      return generateNextSafeJNRLocation(player); // TODO: 24.05.2024 22:05 - not the best way
    }
  }

  public @NotNull Location generateStartJNRLocation(Player player) {
    final JNRConfig config = JNRConfig.get();
    final SecureRandom random = JNRRandomAccessor.getRandom();
    final Location playerLocation = player.getLocation().clone();

    return null;
    // TODO: 24.05.2024 22:07 - implement
  }
}
