package dev.slne.surf.surfserverselector.bukkit;

import dev.slne.surf.surfserverselector.api.SurfServerSelectorApi;
import dev.slne.surf.surfserverselector.bukkit.common.instance.BukkitCommonSurfServerSelectorInstance;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommonBukkitMain extends JavaPlugin {

  private BukkitCommonSurfServerSelectorInstance instance;

  public CommonBukkitMain(BukkitCommonSurfServerSelectorInstance instance) {
    this.instance = instance;
  }

  @OverridingMethodsMustInvokeSuper
  @Override
  public void onLoad() {
    new SurfServerSelectorApi(this.instance);

    this.instance.onLoad();
  }

  @OverridingMethodsMustInvokeSuper
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

  public static JavaPlugin getProvidingPlugin() {
    return getProvidingPlugin(CommonBukkitMain.class);
  }
}