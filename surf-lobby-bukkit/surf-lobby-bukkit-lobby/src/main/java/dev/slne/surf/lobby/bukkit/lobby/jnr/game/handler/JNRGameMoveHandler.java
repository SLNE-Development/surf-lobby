package dev.slne.surf.lobby.bukkit.lobby.jnr.game.handler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class JNRGameMoveHandler {
  public static final JNRGameMoveHandler INSTANCE = new JNRGameMoveHandler();

  private JNRGameMoveHandler() {
  }

  public void handlePlayerMove(Player player, @NotNull Location to) {
//    JNRPlayerDataManager.INSTANCE.
  }
}
