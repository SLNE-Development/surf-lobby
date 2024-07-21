package dev.slne.surf.lobby.core.util;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SurfBlockingQueue<E extends Comparable<E>> {

  private final ObjectList<E> queue;

  public SurfBlockingQueue() {
    this.queue = ObjectLists.synchronize(new ObjectArrayList<>(50));
  }

  public int size() {
    return queue.size();
  }

  public int findIndexOf(Predicate<E> predicate) {
    for (int i = 0; i < queue.size(); i++) {
      if (predicate.test(queue.get(i))) {
        return i;
      }
    }
    return -1;
  }

  public void remove(Predicate<E> predicate) {
    queue.removeIf(predicate);
  }

  public boolean contains(E e) {
    return queue.contains(e);
  }

  public boolean contains(Predicate<E> predicate) {
    return queue.stream().anyMatch(predicate);
  }

  public Stream<E> stream() {
    return queue.stream();
  }

  public void push(E e) {
    queue.add(e);
    queue.sort(Comparator.naturalOrder());
  }

  public Optional<E> pop() {
    if (queue.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(queue.removeFirst());
  }

  public void clear() {
    queue.clear();
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }
}
