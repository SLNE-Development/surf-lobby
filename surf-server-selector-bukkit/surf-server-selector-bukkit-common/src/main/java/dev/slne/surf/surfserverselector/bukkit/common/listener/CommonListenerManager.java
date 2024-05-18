package dev.slne.surf.surfserverselector.bukkit.common.listener;

import dev.slne.surf.surfserverselector.bukkit.CommonBukkitMain;
import dev.slne.surf.surfserverselector.bukkit.common.listener.login.LoginListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public final class CommonListenerManager {
  public static final CommonListenerManager INSTANCE = new CommonListenerManager();

  private CommonListenerManager() {
  }

  public void registerListeners() {
    register(new LoginListener());
  }

  public void unregisterListeners() {
    HandlerList.unregisterAll(CommonBukkitMain.getProvidingPlugin());
  }

  private void register(Listener listener) {
    Bukkit.getPluginManager().registerEvents(listener, CommonBukkitMain.getProvidingPlugin());
  }
}
