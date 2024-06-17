package dev.slne.surf.lobby.bukkit.lobby.jnr.handler;

import dev.slne.surf.lobby.bukkit.lobby.jnr.data.JNRDataManager;
import dev.slne.surf.lobby.bukkit.lobby.jnr.game.JNRGameManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class JNRStartHandler {

  public static final JNRStartHandler INSTANCE = new JNRStartHandler();

  private JNRStartHandler() {
  }

  public void handlePlayerMove(Player player, @NotNull Location to) {
    JNRDataManager.INSTANCE.getStartLocation().ifPresent(startLocation -> {
      if (startLocation.toBlock().equals(to.toBlock())) {
        JNRGameManager.INSTANCE.startGame(player);
      }
    });
  }
}
