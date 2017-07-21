package fr.syst3ms.quarsk.util;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * Created by ARTHUR on 21/07/2017.
 */
public class ReflectionUtils {
    @Nullable
    public static Object getInstanceField(Object instance, String field) {
        Class<?> c = instance.getClass();
        try {
            Field f = c.getDeclaredField(field);
            f.setAccessible(true);
            return f.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
