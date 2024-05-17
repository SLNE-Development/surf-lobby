package dev.slne.surf.surfserverselector.bukkit.common.instance;

import dev.slne.surf.surfserverselector.api.queue.ServerQueueRegistry;
import dev.slne.surf.surfserverselector.bukkit.CommonBukkitMain;
import dev.slne.surf.surfserverselector.bukkit.common.player.BukkitServerPlayerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsEvent;
import java.nio.file.Path;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public abstract class BukkitCommonSurfServerSelectorInstance extends
    CoreSurfServerSelectorInstance {

  public BukkitCommonSurfServerSelectorInstance() {
    super(new BukkitServerPlayerSelectorPlayerManager(), ServerQueueRegistry.createNoOp());
  }

  @OverridingMethodsMustInvokeSuper
  @Override
  public void onLoad() {
    super.onLoad();

    System.err.println("Request");
    new RequestSettingsEvent("").call();
  }

  @OverridingMethodsMustInvokeSuper
  @Override
  public void onEnable() {
    super.onEnable();

    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (final Permissions permissions : Permissions.values()) {
      pluginManager.addPermission(new Permission(permissions.getPermission()));
    }
  }

  @OverridingMethodsMustInvokeSuper
  @Override
  public void onDisable() {
    super.onDisable();
  }

  @Override
  protected ClassLoader getClassLoader() {
    return getInstance().getClassLoader0();
  }

  @Override
  protected Path getDataFolder() {
    return getInstance().getDataFolder().toPath();
  }

  protected abstract CommonBukkitMain getInstance();
}
