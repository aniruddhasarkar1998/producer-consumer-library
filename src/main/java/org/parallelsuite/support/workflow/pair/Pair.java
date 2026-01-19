package org.parallelsuite.support.workflow.pair;

public class Pair<K, V> {

  private K key;

  private V value;

  private Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public static <T, U> Pair<T, U> of(T first, U second) {
    return new Pair<>(first, second);
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }
}
