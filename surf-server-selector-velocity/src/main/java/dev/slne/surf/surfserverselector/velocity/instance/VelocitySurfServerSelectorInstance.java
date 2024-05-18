package dev.slne.surf.surfserverselector.velocity.instance;

import dev.slne.surf.surfapi.velocity.api.SurfVelocityApi;
import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.command.CommandManager;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;
import dev.slne.surf.surfserverselector.velocity.listener.ListenerManager;
import dev.slne.surf.surfserverselector.velocity.player.VelocityServerSelectorPlayerManager;
import dev.slne.surf.surfserverselector.velocity.queue.ServerQueueRegistryImpl;
import dev.slne.surf.surfserverselector.velocity.queue.display.QueueDisplay;
import java.nio.file.Path;

public final class VelocitySurfServerSelectorInstance extends CoreSurfServerSelectorInstance {

  public VelocitySurfServerSelectorInstance() {
    super(new VelocityServerSelectorPlayerManager(), new ServerQueueRegistryImpl());
  }


  @Override
  public void onLoad() {
    super.onLoad();

    SurfVelocityApi.get().createConfig(VelocityConfig.class, getDataFolder(), "config.yml");
  }

  @Override
  public void onEnable() {
    super.onEnable();

    ListenerManager.INSTANCE.registerListeners();
    CommandManager.INSTANCE.registerCommands();
    QueueDisplay.INSTANCE.setup(VelocityMain.getInstance().getServer(), VelocityMain.getInstance());
  }

  @Override
  public void onDisable() {
    super.onDisable();

    QueueDisplay.INSTANCE.destroy();
  }

  @Override
  protected ClassLoader getClassLoader() {
    return VelocityMain.getInstance().getClassLoader();
  }

  @Override
  protected Path getDataFolder() {
    return VelocityMain.getInstance().getDataDirectory();
  }
}