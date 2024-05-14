package dev.slne.surf.surfserverselector.velocity.config;

import dev.slne.surf.surfapi.velocity.api.SurfVelocityApi;
import space.arim.dazzleconf.annote.ConfComments;

public interface VelocityConfig {

  @ConfComments("The prefix for the lobby server name as defined in the velocity.toml file")
  String lobbyServerPrefix();

  static VelocityConfig get() {
    return SurfVelocityApi.get().getConfig(VelocityConfig.class);
  }
}
