package com.accio.api.util;

import jakarta.validation.constraints.NotNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TimeCachedMap<K, V> extends AbstractMap<K, V> {

  private final long timeout;
  private long lastClearedTime = System.currentTimeMillis();
  private final Map<K, V> map = new ConcurrentHashMap<>();

  /**
   * Constructs a new instance.
   *
   * @param millis the cache timeout in milliseconds.
   */
  public TimeCachedMap(final long millis) {
    this.timeout = millis;
  }

  @Override
  public V put(final K key, final V value) {
    return map.put(key, value);
  }

  @Override
  public @NotNull Set<Entry<K, V>> entrySet() {
    if ((System.currentTimeMillis() - lastClearedTime) >= timeout) {
      map.clear();
      lastClearedTime = System.currentTimeMillis();
    }
    return map.entrySet();
  }
}
