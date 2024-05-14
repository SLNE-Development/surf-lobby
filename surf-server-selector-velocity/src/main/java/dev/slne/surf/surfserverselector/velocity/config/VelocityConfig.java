package dev.slne.surf.surfserverselector.velocity.config;

import dev.slne.surf.surfapi.velocity.api.SurfVelocityApi;
import java.util.List;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault.DefaultStrings;

public interface VelocityConfig {

  @ConfComments("The names of the servers that are a lobby server. The name must match the name of the server in the velocity.toml file.")
  @DefaultStrings({"lobby01", "lobby02", "lobby03", "lobby04", "lobby05"})
  List<String> lobbyServerNames();

  static VelocityConfig get() {
    return SurfVelocityApi.get().getConfig(VelocityConfig.class);
  }
}
