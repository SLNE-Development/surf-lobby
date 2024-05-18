package dev.slne.surf.surfserverselector.velocity.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SyncValue<T, L> {

  // @formatter:off
  public static final SyncValue<Boolean, ReadyStateChangeListener> READY_STATE = new SyncValue<>(false, ReadyStateChangeListener::onReadyStateChange);
  public static final SyncValue<Integer, PlayerCountChangeListener> MAX_PLAYER_COUNT = new SyncValue<>(-1, PlayerCountChangeListener::onPlayerCountChange);
  // @formatter:on

  private final T defaultValue;
  private final Changer<T, L> changer;
  private final Map<String, T> values;
  private final List<L> listeners;

  private SyncValue(T defaultValue, Changer<T, L> changer) {
    this.defaultValue = defaultValue;
    this.changer = changer;

    values = new ConcurrentHashMap<>();
    listeners = new ArrayList<>();
  }

  public void sync(String serverName, T value) {
    T previousValue = values.getOrDefault(serverName, defaultValue);

    if (!previousValue.equals(value)) {
      values.put(serverName, value);
      listeners.forEach(listener -> changer.change(listener, serverName, previousValue, value));
    }
  }

  public T get(String serverName) {
    return values.getOrDefault(serverName, defaultValue);
  }

  public void subscribe(L listener) {
    listeners.add(listener);
  }

  private interface Changer<T, L> {

    void change(L listener, String serverName, T oldValue, T newValue);
  }

  @FunctionalInterface
  public interface ReadyStateChangeListener {

    void onReadyStateChange(String serverName, boolean oldReadyState, boolean newReadyState);
  }

  @FunctionalInterface
  public interface PlayerCountChangeListener {

    void onPlayerCountChange(String serverName, int oldPlayerCount, int newPlayerCount);
  }
}
