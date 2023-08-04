package team.moca.camo.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadSafeDistinctMemory<E> {

    private final Map<E, Boolean> map = new ConcurrentHashMap<>();

    public void add(E e) {
        if (map.containsKey(e)) {
            throw new IllegalArgumentException("Element already exists!");
        }

        map.put(e, true);
    }

    public boolean contains(E e) {
        return map.containsKey(e);
    }
}
