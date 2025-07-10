package ee.aleksale.betgame.websocket.registry.contract;

import java.util.Map;

public interface KeyValueRegistry<K, V> {

    void add(K key, V value);
    K getKey(V value);
    void remove(K key);
    Map<K, V> getAll();
    void clear();
}
