package dev.slne.surf.surfserverselector.bukkit.server;

import dev.slne.surf.surfserverselector.bukkit.CommonBukkitMain;
import dev.slne.surf.surfserverselector.bukkit.server.instance.BukkitSurfServerSelectorInstance;
import org.jetbrains.annotations.NotNull;

public final class BukkitMain extends CommonBukkitMain {

  public BukkitMain() {
    super(new BukkitSurfServerSelectorInstance());
  }

  public static @NotNull BukkitMain getInstance() {
    return getPlugin(BukkitMain.class);
  }
}
