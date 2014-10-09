package kga;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class IdUtil {

    public static <V extends Id> Map<Integer, V> convertToMap(Collection<V> collection) {
        final Map<Integer, V> ret = new HashMap<Integer, V>();
        for (V v : collection) {
            ret.put(v.getId(), v);
        }
        return ret;
    }


}
