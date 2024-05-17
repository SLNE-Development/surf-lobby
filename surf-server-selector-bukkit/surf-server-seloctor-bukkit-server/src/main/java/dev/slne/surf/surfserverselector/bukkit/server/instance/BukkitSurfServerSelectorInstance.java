package dev.slne.surf.surfserverselector.bukkit.server.instance;

import dev.slne.surf.surfserverselector.bukkit.common.instance.BukkitCommonSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.bukkit.server.BukkitMain;

public final class BukkitSurfServerSelectorInstance extends BukkitCommonSurfServerSelectorInstance {

  @Override
  public void onLoad() {
    super.onLoad();
  }

  @Override
  public void onEnable() {
    super.onEnable();
  }

  @Override
  public void onDisable() {
    super.onDisable();
  }

  @Override
  protected BukkitMain getInstance() {
    return BukkitMain.getInstance();
  }
}
