package com.test.bus.common.pojo.message;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

/**
 * @author 费世程
 * @date 2021/2/26 14:31
 */
public class EventHeaders implements Map<String, Set<String>>, Serializable {

  private final Map<String, Set<String>> targetMap;

  private EventHeaders() {
    this.targetMap = new LinkedHashMap<>();
  }

  public static EventHeaders create() {
    return new EventHeaders();
  }

  public EventHeaders add(String key, String value) {
    Set<String> currentValues = this.targetMap.computeIfAbsent(key, k -> new HashSet<>());
    currentValues.add(value);
    return this;
  }

  public EventHeaders addAll(String key, Set<String> values) {
    Set<String> currentValues = this.targetMap.computeIfAbsent(key, k -> new HashSet<>());
    currentValues.addAll(values);
    return this;
  }

  @Override
  public int size() {
    return this.targetMap.size();
  }

  @Override
  public boolean isEmpty() {
    return this.targetMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return this.targetMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return this.targetMap.containsValue(value);
  }

  @Override
  public Set<String> get(Object key) {
    return this.targetMap.get(key);
  }

  @Override
  public Set<String> put(String key, Set<String> value) {
    return this.targetMap.put(key, value);
  }

  @Override
  public Set<String> remove(Object key) {
    return this.targetMap.remove(key);
  }

  @Override
  public void putAll(Map<? extends String, ? extends Set<String>> m) {
    this.targetMap.putAll(m);
  }

  @Override
  public void clear() {
    this.targetMap.clear();
  }

  @NotNull
  @Override
  public Set<String> keySet() {
    return this.targetMap.keySet();
  }

  @NotNull
  @Override
  public Collection<Set<String>> values() {
    return this.targetMap.values();
  }

  @NotNull
  @Override
  public Set<Entry<String, Set<String>>> entrySet() {
    return this.targetMap.entrySet();
  }
}
