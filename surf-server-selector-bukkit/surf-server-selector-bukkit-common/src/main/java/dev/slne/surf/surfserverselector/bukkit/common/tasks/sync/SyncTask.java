package dev.slne.surf.surfserverselector.bukkit.common.tasks.sync;

import dev.slne.surf.surfserverselector.core.spring.redis.events.server.sync.MaxPlayerCountSync;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public final class SyncTask extends BukkitRunnable {

  @Override
  public void run() {
    sync();
  }

  public static void sync() {
    new MaxPlayerCountSync(Bukkit.getMaxPlayers()).call();
  }
}