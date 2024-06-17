package dev.slne.surf.lobby.bukkit;

import dev.slne.surf.lobby.api.LobbyApi;
import dev.slne.surf.lobby.bukkit.common.instance.BukkitCommonLobbyInstance;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommonBukkitMain extends JavaPlugin {

  private BukkitCommonLobbyInstance instance;

  public CommonBukkitMain(BukkitCommonLobbyInstance instance) {
    this.instance = instance;
  }

  @OverridingMethodsMustInvokeSuper
  @Override
  public void onLoad() {
    new LobbyApi(this.instance);

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