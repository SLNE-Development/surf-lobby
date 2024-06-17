package dev.slne.surf.lobby.bukkit.lobby.instance;

import dev.slne.surf.lobby.bukkit.common.instance.BukkitCommonLobbyInstance;
import dev.slne.surf.lobby.bukkit.lobby.BukkitMain;
import dev.slne.surf.lobby.bukkit.lobby.listener.ListenerManager;
import org.jetbrains.annotations.NotNull;

public final class BukkitLobbyInstance extends BukkitCommonLobbyInstance {

  @Override
  public void onLoad() {
    super.onLoad();
  }

  @Override
  public void onEnable() {
    super.onEnable();

    ListenerManager.INSTANCE.registerListeners();
  }

  @Override
  public void onDisable() {
    super.onDisable();

    ListenerManager.INSTANCE.unregisterListeners();
  }

  @Override
  protected @NotNull BukkitMain getInstance() {
    return BukkitMain.getInstance();
  }
}
