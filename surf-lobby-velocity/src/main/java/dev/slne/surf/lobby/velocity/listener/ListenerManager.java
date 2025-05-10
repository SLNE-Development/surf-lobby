package dev.slne.surf.lobby.velocity.listener;

import dev.slne.surf.lobby.velocity.VelocityMain;
import dev.slne.surf.lobby.velocity.listener.player.PlayerQuitEventListener;
import dev.slne.surf.lobby.velocity.listener.player.PlayerSwitchServerEventListener;

public final class ListenerManager {

  public static final ListenerManager INSTANCE = new ListenerManager();

  private ListenerManager() {
  }

  public void registerListeners() {
    register(new PlayerQuitEventListener());
    register(new PlayerSwitchServerEventListener());
  }

  private void register(Object listener) {
    final VelocityMain plugin = VelocityMain.getInstance();
    plugin.getServer().getEventManager().register(plugin, listener);
  }
}
