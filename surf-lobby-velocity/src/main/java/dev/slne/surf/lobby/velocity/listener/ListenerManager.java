package dev.slne.surf.lobby.velocity.listener;

import dev.slne.surf.lobby.velocity.VelocityMain;

public final class ListenerManager {

  public static final ListenerManager INSTANCE = new ListenerManager();

  private ListenerManager() {
  }

  public void registerListeners() {
  }

  private void register(Object listener) {
    final VelocityMain plugin = VelocityMain.getInstance();
    plugin.getServer().getEventManager().register(plugin, listener);
  }
}
