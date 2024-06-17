package dev.slne.surf.lobby.bukkit.common.instance;

import dev.slne.surf.lobby.api.queue.ServerQueueRegistry;
import dev.slne.surf.lobby.bukkit.CommonBukkitMain;
import dev.slne.surf.lobby.bukkit.common.listener.CommonListenerManager;
import dev.slne.surf.lobby.bukkit.common.player.BukkitLobbyPlayerManager;
import dev.slne.surf.lobby.bukkit.common.tasks.sync.SyncTask;
import dev.slne.surf.lobby.core.instance.CoreLobbyInstance;
import dev.slne.surf.lobby.core.permissions.Permissions;
import dev.slne.surf.lobby.core.spring.redis.events.server.lobby.RequestSettingsEvent;
import dev.slne.surf.lobby.core.spring.redis.events.server.sync.ReadyStateSync;
import java.nio.file.Path;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public abstract class BukkitCommonLobbyInstance extends
    CoreLobbyInstance {

  public BukkitCommonLobbyInstance() {
    super(new BukkitLobbyPlayerManager(), ServerQueueRegistry.createNoOp());
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
    Bukkit.getScheduler().runTaskLater(getInstance(), this::afterEnable, 1);
  }

  @OverridingMethodsMustInvokeSuper
  public void afterEnable() {
    SyncTask.sync();
    new ReadyStateSync(true).call();
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
