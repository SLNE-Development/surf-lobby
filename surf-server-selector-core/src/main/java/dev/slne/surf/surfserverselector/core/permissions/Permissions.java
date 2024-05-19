package dev.slne.surf.surfserverselector.core.permissions;

import org.apache.commons.lang3.builder.ToStringBuilder;

public enum Permissions {
  BYPASS_QUEUE_PERMISSION("surf.server.selector.queue.bypass"),
  BYPASS_PLAYER_LIMIT_PERMISSION("surf.server.selector.player-limit.bypass"),
  COMMAND_HUB_SELF("surf.server.selector.command.hub.self"),
  COMMAND_HUB_OTHERS("surf.server.selector.command.hub.others"),
  COMMAND_SWITCH_EVENT_SERVER_STATE("surf.server.selector.command.switch-event-server-state"),;

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
