package fr.syst3ms.quarsk.util;

import java.lang.reflect.Array;

/**
 * Created by ARTHUR on 22/02/2017.
 */
@SuppressWarnings("unused")
public final class ReflectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> Class<T[]> getArrayClass(Class<? extends T> c) {
        return (Class<T[]>) Array.newInstance(c, 0).getClass();
    }

    public static Class<?> getNMSClass(String nmsClassString) throws ClassNotFoundException {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        String name = "net.minecraft.server." + version + nmsClassString;
        return Class.forName(name);
    }
}
