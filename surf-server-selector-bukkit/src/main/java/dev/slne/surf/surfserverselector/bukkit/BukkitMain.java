package dev.slne.surf.surfserverselector.bukkit;

import dev.slne.surf.surfserverselector.bukkit.instance.BukkitSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitMain extends JavaPlugin {

  private BukkitSurfServerSelectorInstance instance;

  @Override
  public void onLoad() {
    this.instance = new BukkitSurfServerSelectorInstance();
    new SurfServerSelectorApi(this.instance);

    this.instance.onLoad();
  }

  @Override
  public void onEnable() {
    this.instance.onEnable();
  }

  @Override
  public void onDisable() {
    this.instance.onDisable();
  }

  public ClassLoader getClassLoader0() {
    return getClassLoader();
  }

  public static BukkitMain getInstance() {
    return getPlugin(BukkitMain.class);
  }
}