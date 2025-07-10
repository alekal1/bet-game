package ee.aleksale.betgame.websocket.registry.contract;

import java.util.Set;

public interface SetRegistry<T> {

    void add(T value);
    void remove(T value);
    Set<T> getAll();
    void clear();
}
