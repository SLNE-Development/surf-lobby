package dev.slne.surf.lobby.velocity.config;

import dev.slne.surf.surfapi.velocity.api.SurfVelocityApi;
import space.arim.dazzleconf.annote.ConfComments;
import space.arim.dazzleconf.annote.ConfDefault.DefaultBoolean;
import space.arim.dazzleconf.annote.ConfDefault.DefaultString;

public interface VelocityConfig {

  @ConfComments("The prefix for the lobby server name as defined in the velocity.toml file")
  @DefaultString("lobby-")
  String lobbyServerPrefix();

  @ConfComments("The Event server name as defined in the velocity.toml file for the curren running event")
  @DefaultString("event")
  String currentEventServer();

  @ConfComments("Whether the event server is enabled")
  @DefaultBoolean(false)
  boolean eventServerEnabled();

  @ConfComments("The name of the community server as defined in the velocity.toml file")
  @DefaultString("community-server")
  String communityServerName();

  static VelocityConfig get() {
    return SurfVelocityApi.get().getConfig(VelocityConfig.class);
  }
}
