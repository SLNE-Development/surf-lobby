package dev.slne.surf.surfserverselector.bukkit.listener;

import dev.slne.surf.surfserverselector.bukkit.BukkitMain;
import dev.slne.surf.surfserverselector.bukkit.listener.lobby.HotbarItemClickEventListener;
import dev.slne.surf.surfserverselector.bukkit.listener.lobby.PlayerJoinListener;
import dev.slne.surf.surfserverselector.bukkit.listener.lobby.PlayerQuitEventListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public final class ListenerManager {

  public static final ListenerManager INSTANCE = new ListenerManager();

  private ListenerManager() {
  }

  public void registerListeners() {

    if (BukkitMain.getInstance().isLobby()) {
      register(new HotbarItemClickEventListener());
      register(new PlayerJoinListener());
      register(new PlayerQuitEventListener());
    }
  }

  public void unregisterListeners() {
    HandlerList.unregisterAll(BukkitMain.getInstance());
  }

  private void register(Listener listener) {
    Bukkit.getPluginManager().registerEvents(listener, BukkitMain.getInstance());
  }
}