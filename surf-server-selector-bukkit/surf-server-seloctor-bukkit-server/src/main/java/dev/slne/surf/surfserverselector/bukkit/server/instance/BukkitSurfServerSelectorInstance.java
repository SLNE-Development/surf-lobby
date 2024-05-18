package dev.slne.surf.surfserverselector.bukkit.server.instance;

import dev.slne.surf.surfserverselector.bukkit.common.instance.BukkitCommonSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.bukkit.server.BukkitMain;
import dev.slne.surf.surfserverselector.bukkit.server.command.CommandManager;

public final class BukkitSurfServerSelectorInstance extends BukkitCommonSurfServerSelectorInstance {

  @Override
  public void onLoad() {
    super.onLoad();

    CommandManager.INSTANCE.registerCommands();
  }

  @Override
  public void onEnable() {
    super.onEnable();
  }

  @Override
  public void onDisable() {
    super.onDisable();

    CommandManager.INSTANCE.unregisterCommands();
  }

  @Override
  protected BukkitMain getInstance() {
    return BukkitMain.getInstance();
  }
}
