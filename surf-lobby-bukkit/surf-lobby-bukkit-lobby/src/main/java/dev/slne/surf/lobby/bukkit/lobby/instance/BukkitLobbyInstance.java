package dev.slne.surf.lobby.bukkit.lobby.instance;

import dev.slne.surf.lobby.bukkit.common.instance.BukkitCommonLobbyInstance;
import dev.slne.surf.lobby.bukkit.lobby.BukkitMain;
import dev.slne.surf.lobby.bukkit.lobby.jnr.JNRManager;
import dev.slne.surf.lobby.bukkit.lobby.listener.ListenerManager;
import dev.slne.surf.lobby.bukkit.lobby.scheduler.LobbyScheduler;
import org.jetbrains.annotations.NotNull;

/**
 * The type Bukkit lobby instance.
 */
public final class BukkitLobbyInstance extends BukkitCommonLobbyInstance {

  private LobbyScheduler scheduler;

  @Override
  public void onLoad() {
    super.onLoad();

    JNRManager.INSTANCE.onLoad();

    scheduler = new LobbyScheduler();
  }

  @Override
  public void onEnable() {
    super.onEnable();

    ListenerManager.INSTANCE.registerListeners();
    JNRManager.INSTANCE.onEnable();

    scheduler.start();
  }

  @Override
  public void afterEnable() {
    super.afterEnable();

    JNRManager.INSTANCE.afterEnable();
  }

  @Override
  public void onDisable() {
    super.onDisable();

    ListenerManager.INSTANCE.unregisterListeners();
    JNRManager.INSTANCE.onDisable();

    scheduler.stop();
  }

  @Override
  protected @NotNull BukkitMain getInstance() {
    return BukkitMain.getInstance();
  }
}
