package dev.slne.surf.lobby.bukkit.lobby.instance;

import dev.slne.surf.lobby.bukkit.common.instance.BukkitCommonLobbyInstance;
import dev.slne.surf.lobby.bukkit.lobby.BukkitMain;
import dev.slne.surf.lobby.bukkit.lobby.jnr.JnrManager;
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

    scheduler = new LobbyScheduler();
  }

  @Override
  public void onEnable() {
    super.onEnable();

    ListenerManager.INSTANCE.registerListeners();
    JnrManager.INSTANCE.generateJnr();

    scheduler.start();
  }

  @Override
  public void afterEnable() {
    super.afterEnable();
  }

  @Override
  public void onDisable() {
    super.onDisable();

    ListenerManager.INSTANCE.unregisterListeners();

    scheduler.stop();
  }

  @Override
  protected @NotNull BukkitMain getInstance() {
    return BukkitMain.getInstance();
  }
}
