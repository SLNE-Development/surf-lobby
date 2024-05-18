package dev.slne.surf.surfserverselector.bukkit.common.instance;

import dev.slne.surf.surfserverselector.api.queue.ServerQueueRegistry;
import dev.slne.surf.surfserverselector.bukkit.CommonBukkitMain;
import dev.slne.surf.surfserverselector.bukkit.common.listener.CommonListenerManager;
import dev.slne.surf.surfserverselector.bukkit.common.player.BukkitServerPlayerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.bukkit.common.tasks.sync.SyncTask;
import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsEvent;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.sync.ReadyStateSync;
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

    new RequestSettingsEvent("").call();
  }

  @OverridingMethodsMustInvokeSuper
  @Override
  public void onEnable() {
    super.onEnable();

    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (final Permissions permissions : Permissions.values()) {
      try {
        pluginManager.addPermission(new Permission(permissions.getPermission()));
      } catch (final IllegalArgumentException e) {
        // Permission already exists
      }
    }

    CommonListenerManager.INSTANCE.registerListeners();

    new SyncTask().runTaskTimerAsynchronously(getInstance(), 0, 20 * 5);

    Bukkit.getScheduler().runTaskLater(getInstance(), () -> {
      SyncTask.sync();
      new ReadyStateSync(true).call();
    }, 1);
  }

  @OverridingMethodsMustInvokeSuper
  @Override
  public void onDisable() {
    new ReadyStateSync(false).call();
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
