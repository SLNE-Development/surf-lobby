package dev.slne.surf.lobby.bukkit.lobby.jnr.game;

import dev.slne.surf.lobby.bukkit.lobby.jnr.data.JNRDataManager;
import dev.slne.surf.lobby.bukkit.lobby.jnr.game.block.JNRBlockManager;
import dev.slne.surf.lobby.bukkit.lobby.jnr.game.location.LocationGenerator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class JNRGameManager {

  public static final JNRGameManager INSTANCE = new JNRGameManager();

  private JNRGameManager() {
  }

  public void startGame(Player player) {
    if (JNRDataManager.INSTANCE.isRunningPlayer(player)) {
      return; // TODO: 24.05.2024 22:26 - send message
    }

    JNRDataManager.INSTANCE.addRunningPlayer(player);
    final Location startLocation = LocationGenerator.INSTANCE.generateStartJNRLocation(player);

    JNRBlockManager.INSTANCE.placeBlock(player, startLocation);
    player.teleport(startLocation.clone().add(0, 1, 0));

    final Location nextLocation = LocationGenerator.INSTANCE.generateNextSafeJNRLocation(player);
    JNRBlockManager.INSTANCE.placeBlock(player, nextLocation);
  }

  public void endGame(Player player) {
    // End the game
  }
}
