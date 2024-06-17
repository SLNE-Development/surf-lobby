package dev.slne.surf.lobby.bukkit.lobby.jnr.listener;

import dev.slne.surf.lobby.bukkit.lobby.jnr.data.JNRPlayerDataManager;
import dev.slne.surf.lobby.bukkit.lobby.jnr.game.handler.JNRGameMoveHandler;
import dev.slne.surf.lobby.bukkit.lobby.jnr.handler.JNRStartHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener implements Listener {

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    if (!event.hasChangedBlock()) {
      return;
    }

    final Player player = event.getPlayer();

    if (JNRPlayerDataManager.INSTANCE.isInGame(player)) {
      JNRGameMoveHandler.INSTANCE.handlePlayerMove(player, event.getTo());
    } else {
      JNRStartHandler.INSTANCE.handlePlayerMove(player, event.getTo());
    }
  }
}
