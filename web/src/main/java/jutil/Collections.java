package jutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Collections {

  public static <T extends Comparable<? super T>> List<T> sort(final Collection<T> collection) {
    List<T> ret = new ArrayList<T>(collection);
    java.util.Collections.sort(ret);
    return ret;
  }

  public static <T extends Comparable<? super T>> List<T> sort(final Map<?, T> collection) {
    return sort(collection.values());
  }
}
