package dev.slne.surf.surfserverselector.core.permissions;

import org.apache.commons.lang3.builder.ToStringBuilder;

public enum Permissions {
  BYPASS_QUEUE_PERMISSION("surf.server.selector.queue.bypass");

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
