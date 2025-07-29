package dev.slne.surf.lobby.core.permissions;

import org.apache.commons.lang3.builder.ToStringBuilder;

public enum Permissions {
  BYPASS_PLAYER_LIMIT_PERMISSION("surf.lobby.player-limit.bypass"),
  BYPASS_HUB_ATTACK_PERMISSION("surf.lobby.hub.bypass.attack"),
  COMMAND_HUB_SELF("surf.lobby.command.hub.self"),
  COMMAND_HUB_OTHERS("surf.lobby.command.hub.others"),
  COMMAND_SWITCH_EVENT_SERVER_STATE("surf.lobby.command.switch-event-server-state"),

  FEATURE_USE_PLAYER_PUSHBACK_ATTACK("surf.lobby.feature.use.player-pushback-attack"),
  FEATURE_USE_PLAYER_PUSHBACK_ITEM("surf.lobby.feature.use.player-pushback-item"),

  ;

  private final String permission;

  Permissions(String permission) {
    this.permission = permission;
  }

  public String getPermission() {
    return permission;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
