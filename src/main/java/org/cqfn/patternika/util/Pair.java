package org.cqfn.patternika.util;

/**
 * Simple pair in case someone needs to use (key, val) objects without creating a map.
 *
 * @param <K> key type.
 * @param <V> value type.
 *
 * @since 2019/11/21
 */
public final class Pair<K, V> {
    /** The key. */
    private final K key;
    /** The value. */
    private final V val;

    /**
     * Constructor.
     *
     * @param key the key.
     * @param val the value.
     */
    public Pair(final K key, final V val) {
        this.key = key;
        this.val = val;
    }

    /**
     * Returns the key.
     *
     * @return key.
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the value.
     *
     * @return value.
     */
    public V getVal() {
        return val;
    }

}
