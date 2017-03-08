package fr.syst3ms.quarsk.util;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings("unused")
public class ListUtils {

    public static <T, U> List<U> mapList(List<T> list, Function<T, U> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static <T> T randomElement(T[] array) {
        return array[new Random().nextInt(array.length)];
    }

    public static <T> Collection<T> eachOfDoubleArray(Collection<T[]> doubleArray) {
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

    public static <T> Collection<T> collectionFromIterable(Iterable<T> iterable) {
        return Lists.newArrayList(iterable);
    }

    @SafeVarargs
    public static <T> Collection<T> collection(T... elements) {
        return Arrays.asList(elements);
    }

    @SafeVarargs
    public static <T> Iterator<T> iterator(T... elements) {
        return collection(elements).iterator();
    }

    @SafeVarargs
    public static <T> Iterable<T> iterable(T... elements) {
        return collection(elements);
    }

    public static <T> Iterable<T> reversed(Iterable<T> coll) {
        List<T> reversed = new ArrayList<>();
        for (int i = size(coll) - 1; i >= 0; i--)
            reversed.add(new ArrayList<>(fromIterable(coll)).get(i));
        return reversed;
    }

    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return elements(iterable).stream();
    }

    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return elements(iterator).stream();
    }

    public static <T> Iterator<T> fromEnumeration(Enumeration<T> enumeration) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public T next() {
                return enumeration.nextElement();
            }
        };
    }

    public static <T> Collection<T> elements(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        while (iterator.hasNext())
            list.add(iterator.next());
        return list;
    }

    public static <T> Collection<T> elements(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        iterable.forEach(list::add);
        return list;
    }

    public static <T> Collection<T> fromIterable(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T t : iterable)
            list.add(t);
        return list;
    }

    public static <T> int size(Iterable<T> iterable) {
        int size = 0;
        for (T t : iterable)
            size++;
        return size;
    }

    public static <T> Collection<T> nestedElements(Collection<Collection<T>> list) {
        List<T> each = new ArrayList<>();
        for (Collection<T> coll : list)
            coll.forEach(each::add);
        return each;
    }

    public static <T> Collection<T> nestedElements(T[][] nested) {
        List<T> list = new ArrayList<>();
        forArray(t -> forArray(list::add, t), nested);
        return list;
     }

    @SafeVarargs
    public static <T> void forArray(Consumer<? super T> consumer, T... array) {
        Arrays.asList(array).forEach(consumer);
    }

    public static <T> boolean anyMatch(Predicate<? super T> predicate, Collection<T> coll) {
        return coll.stream().anyMatch(predicate);
    }

    @SafeVarargs
    public static <T> boolean anyMatch(Predicate<? super T> predicate, T... array) {
        return Arrays.stream(array).anyMatch(predicate);
    }

    public static <T> boolean allMatch(Predicate<? super T> predicate, Collection<T> coll) {
        return coll.stream().allMatch(predicate);
    }

    @SafeVarargs
    public static <T> boolean allMatch(Predicate<? super T> predicate, T... array) {
        return Arrays.stream(array).allMatch(predicate);
    }
}
