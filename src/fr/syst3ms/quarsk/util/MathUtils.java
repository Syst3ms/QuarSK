package fr.syst3ms.quarsk.util;

import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by ARTHUR on 22/02/2017.
 */
@SuppressWarnings("unused")
public final class MathUtils {

    public static Number getDecimalPart(Number number) {
        return number.doubleValue() - Math.floor(number.doubleValue());
    }

    public static Number getIntegerPart(Number number) {
        return number.doubleValue() - getDecimalPart(number).doubleValue();
    }

    public static Number max(Number... nums) {
        Optional<Number> reduce = Stream.of(nums)
                                        .reduce((n, n1) -> n.doubleValue() > n1.doubleValue() ? n : n1);
        assert reduce.isPresent();
        return reduce.get();
    }

    public static Number maxWithRandom(Number... nums) {
        return ListUtils.randomElement(Stream.of(nums)
                     .filter(num -> num.equals(MathUtils.max(nums)))
                     .toArray(Number[]::new)
        );
    }

    public static int mean(int... nums) {
        return IntStream.of(nums).sum() / nums.length;
    }

    public static double mean(double... nums) {
        return DoubleStream.of(nums).sum() / nums.length;
    }
}
