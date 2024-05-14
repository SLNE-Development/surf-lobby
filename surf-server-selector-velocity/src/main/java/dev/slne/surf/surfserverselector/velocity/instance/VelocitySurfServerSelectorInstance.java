package dev.slne.surf.surfserverselector.velocity.instance;

import dev.slne.surf.surfapi.velocity.api.SurfVelocityApi;
import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;
import dev.slne.surf.surfserverselector.velocity.config.VelocityConfig;
import dev.slne.surf.surfserverselector.velocity.player.VelocityServerSelectorPlayerManager;
import java.nio.file.Path;

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
  protected ClassLoader getClassLoader() {
    return VelocityMain.getInstance().getClassLoader();
  }

  @Override
  protected Path getDataFolder() {
    return VelocityMain.getInstance().getDataDirectory();
  }
}