package dev.slne.surf.surfserverselector.bukkit.instance;

import dev.slne.surf.surfserverselector.api.queue.ServerQueueRegistry;
import dev.slne.surf.surfserverselector.bukkit.player.BukkitServerPlayerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.bukkit.BukkitMain;
import dev.slne.surf.surfserverselector.core.permissions.Permissions;
import java.nio.file.Path;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public final class BukkitSurfServerSelectorInstance extends CoreSurfServerSelectorInstance {

  public BukkitSurfServerSelectorInstance() {
    super(new BukkitServerPlayerSelectorPlayerManager(), ServerQueueRegistry.createNoOp());
  }

  @Override
  public void onEnable() {
    super.onEnable();

    final PluginManager pluginManager = Bukkit.getPluginManager();
    for (final Permissions permissions : Permissions.values()) {
      pluginManager.addPermission(new Permission(permissions.getPermission()));
    }
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