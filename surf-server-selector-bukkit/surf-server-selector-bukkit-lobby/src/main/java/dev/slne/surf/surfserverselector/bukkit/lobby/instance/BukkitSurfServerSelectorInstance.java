package dev.slne.surf.surfserverselector.bukkit.lobby.instance;

import dev.slne.surf.surfserverselector.bukkit.common.instance.BukkitCommonSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.bukkit.lobby.BukkitMain;
import dev.slne.surf.surfserverselector.bukkit.lobby.listener.ListenerManager;
import org.jetbrains.annotations.NotNull;

public final class BukkitSurfServerSelectorInstance extends BukkitCommonSurfServerSelectorInstance {

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
