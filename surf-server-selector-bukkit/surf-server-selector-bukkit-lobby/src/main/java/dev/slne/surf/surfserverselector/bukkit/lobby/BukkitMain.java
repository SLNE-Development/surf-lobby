package dev.slne.surf.surfserverselector.bukkit.lobby;

import dev.slne.surf.surfserverselector.bukkit.CommonBukkitMain;
import dev.slne.surf.surfserverselector.bukkit.lobby.instance.BukkitSurfServerSelectorInstance;
import org.jetbrains.annotations.NotNull;

public final class BukkitMain extends CommonBukkitMain {

  public BukkitMain() {
    super(new BukkitSurfServerSelectorInstance());
  }

  @Override
  public void onLoad() {
    super.onLoad();


  }

  @Override
  public void onEnable() {
    super.onEnable();
  }

  public static @NotNull BukkitMain getInstance() {
    return getPlugin(BukkitMain.class);
  }
}
