package dev.slne.surf.surfserverselector.bukkit.instance;

import dev.slne.surf.surfserverselector.bukkit.player.BukkitServerPlayerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.bukkit.BukkitMain;

public final class BukkitSurfServerSelectorInstance extends CoreSurfServerSelectorInstance {

  public BukkitSurfServerSelectorInstance() {
    super(new BukkitServerPlayerSelectorPlayerManager());
  }

  @Override
  protected ClassLoader getClassLoader() {
    return BukkitMain.getInstance().getClassLoader0();
  }
}