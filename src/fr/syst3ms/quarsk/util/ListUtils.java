package fr.syst3ms.quarsk.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings("unused")
public class ListUtils {
    public static <T, U> List<U> mapCollection(Function<T, U> mapper, Collection<T> list) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T> T randomElement(T[] array) {
        return array[new Random().nextInt(array.length)];
    }

    public static <T> Collection<T> eachOf(Collection<T[]> doubleArray) {
        List<T> list = new ArrayList<>();
        doubleArray.forEach(tArray -> Collections.addAll(list, tArray));
        return list;
    }

    public static <T> T getCollElement(Collection<T> coll, int index) {
        return new ArrayList<>(coll).get(index);
    }

    public static <T> T getMapKey(Map<T, ?> map, int index) {
        return getCollElement(map.keySet(), index);
    }
}
