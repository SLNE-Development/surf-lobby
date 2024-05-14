package dev.slne.surf.surfserverselector.velocity.instance;

import dev.slne.surf.surfserverselector.core.instance.CoreSurfServerSelectorInstance;
import dev.slne.surf.surfserverselector.velocity.VelocityMain;

public final class VelocitySurfServerSelectorInstance extends CoreSurfServerSelectorInstance {

  public VelocitySurfServerSelectorInstance() {
    super();
  }

  @Override
  protected ClassLoader getClassLoader() {
    return VelocityMain.getInstance().getClassLoader();
  }
}