package dev.slne.surf.lobby.velocity.instance;

import dev.slne.surf.lobby.velocity.queue.ServerQueueRegistryImpl;
import dev.slne.surf.surfapi.velocity.api.SurfVelocityApi;
import dev.slne.surf.lobby.core.instance.CoreLobbyInstance;
import dev.slne.surf.lobby.velocity.VelocityMain;
import dev.slne.surf.lobby.velocity.command.CommandManager;
import dev.slne.surf.lobby.velocity.config.VelocityConfig;
import dev.slne.surf.lobby.velocity.config.VelocityPersistentData;
import dev.slne.surf.lobby.velocity.listener.ListenerManager;
import dev.slne.surf.lobby.velocity.player.VelocityLobbyPlayerManager;
import dev.slne.surf.lobby.velocity.queue.display.QueueDisplay;
import java.nio.file.Path;
import org.spongepowered.configurate.ConfigurateException;

public final class VelocityLobbyInstance extends CoreLobbyInstance {

  public VelocityLobbyInstance() {
    super(new VelocityLobbyPlayerManager(), new ServerQueueRegistryImpl());
  }


  @Override
  public void onLoad() {
    super.onLoad();

    SurfVelocityApi.get().createConfig(VelocityConfig.class, getDataFolder(), "config.yml");
    try {
      VelocityPersistentData.load();
    } catch (ConfigurateException e) {
      LOGGER.error("Failed to load persistent data", e);

      throw new RuntimeException(e);
    }
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

    try {
      VelocityPersistentData.save();
    } catch (ConfigurateException e) {
      LOGGER.error("Failed to save persistent data", e);
      throw new RuntimeException(e);
    }
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