package dev.slne.surf.surfserverselector.velocity.instance;

import com.velocitypowered.api.event.permission.PermissionsSetupEvent;
import dev.slne.surf.surfapi.velocity.api.SurfVelocityApi;
import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;
import dev.slne.surf.surfserverselector.velocity.listener.ListenerManager;
import dev.slne.surf.surfserverselector.velocity.player.VelocityServerSelectorPlayerManager;
import java.nio.file.Path;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;

public final class VelocitySurfServerSelectorInstance extends CoreSurfServerSelectorInstance {

  public VelocitySurfServerSelectorInstance() {
    super(new VelocityServerSelectorPlayerManager());
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