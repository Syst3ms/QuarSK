package fr.syst3ms.quarsk.util;

import ch.njol.skript.util.Version;

/**
 * Created by ARTHUR on 22/02/2017.
 */
@SuppressWarnings("unused")
public class MathUtils {

    public static Number getDecimalPart(Number number) {
        return number.doubleValue() - Math.floor(number.doubleValue());
    }

    public static Number getIntegerPart(Number number) {
        return number.doubleValue() - getDecimalPart(number).doubleValue();
    }
}
