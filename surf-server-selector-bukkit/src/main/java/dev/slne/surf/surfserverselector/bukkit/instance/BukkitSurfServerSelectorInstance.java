package dev.slne.surf.surfserverselector.bukkit.instance;

import dev.slne.surf.surfserverselector.api.queue.ServerQueueRegistry;
import dev.slne.surf.surfserverselector.bukkit.BukkitMain;
import dev.slne.surf.surfserverselector.bukkit.listener.ListenerManager;
import dev.slne.surf.surfserverselector.bukkit.player.BukkitServerPlayerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import dev.slne.surf.surfserverselector.core.spring.redis.events.server.lobby.RequestSettingsEvent;
import java.nio.file.Path;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public final class BukkitSurfServerSelectorInstance extends CoreSurfServerSelectorInstance {

  public BukkitSurfServerSelectorInstance() {
    super(new BukkitServerPlayerSelectorPlayerManager(), ServerQueueRegistry.createNoOp());
  }

  @Override
  public void onLoad() {
    super.onLoad();

    new RequestSettingsEvent("").call();
  }

  @Override
  public void onEnable() {
    super.onEnable();

    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (final Permissions permissions : Permissions.values()) {
      pluginManager.addPermission(new Permission(permissions.getPermission()));
    }

    ListenerManager.INSTANCE.registerListeners();
  }

  @Override
  public void onDisable() {
    super.onDisable();

    ListenerManager.INSTANCE.unregisterListeners();
  }

  @Override
  protected ClassLoader getClassLoader() {
    return BukkitMain.getInstance().getClassLoader0();
  }

  @Override
  protected Path getDataFolder() {
    return BukkitMain.getInstance().getDataFolder().toPath();
  }
}