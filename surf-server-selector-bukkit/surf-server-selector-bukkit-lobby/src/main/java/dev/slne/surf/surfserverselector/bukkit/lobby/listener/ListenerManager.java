package dev.slne.surf.surfserverselector.bukkit.lobby.listener;

import dev.slne.surf.surfserverselector.bukkit.lobby.BukkitMain;
import dev.slne.surf.surfserverselector.bukkit.lobby.listener.hotbar.DestroyPlayerHotbarListener;
import dev.slne.surf.surfserverselector.bukkit.lobby.listener.hotbar.HotbarItemClickEventListener;
import dev.slne.surf.surfserverselector.bukkit.lobby.listener.hotbar.PopulatePlayerHotbarListener;
import dev.slne.surf.surfserverselector.bukkit.lobby.listener.qole.DoubleJumpListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public final class ListenerManager {

  public static final ListenerManager INSTANCE = new ListenerManager();

  private ListenerManager() {
  }

  public void registerListeners() {
    register(new HotbarItemClickEventListener());
    register(new PopulatePlayerHotbarListener());
    register(new DestroyPlayerHotbarListener());

    register(new DoubleJumpListener());
  }

  public void unregisterListeners() {
    HandlerList.unregisterAll(BukkitMain.getInstance());
  }

  private void register(Listener listener) {
    Bukkit.getPluginManager().registerEvents(listener, BukkitMain.getInstance());
  }
}