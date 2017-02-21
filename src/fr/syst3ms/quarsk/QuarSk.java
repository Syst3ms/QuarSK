package fr.syst3ms.quarsk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.PotionEffectUtils;
import com.sun.istack.internal.Nullable;
import fr.syst3ms.quarsk.classes.EnumType;
import fr.syst3ms.quarsk.util.ListUtils;
import fr.syst3ms.quarsk.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "unchecked"})
public class QuarSk extends JavaPlugin {

    private static final double RAD_TO_DEG = 180 / Math.PI;
    private static SkriptAddon addonInstance;
    private static HashMap<Class<?>, String[]> events = new HashMap<>();
    private static HashMap<Class<?>, String[]> conditions = new HashMap<>();
    private static HashMap<Class<?>, String[]> effects = new HashMap<>();
    private static HashMap<Class<?>, String[]> expressions = new HashMap<>();
    private static JavaPlugin plugin;

    public void onEnable() {
        if (getServer().getBukkitVersion().startsWith("1.7")) {
            getLogger().log(Level.INFO, "");
            getServer().getPluginManager().disablePlugin(this);
        }
        plugin = this;
        normalRegister();
        if (generateFolder())
            getLogger().log(Level.INFO, "Created QuarSk's folder !");
        if (generateSyntaxFile())
            getLogger().log(Level.INFO, "Generated QuarSk's syntax file !");
        getLogger().log(Level.INFO, "Registered " + events.size() + " events, " + conditions.size() + " conditions, " + effects.size() + " effects and " + expressions.size() + " expressions ! Good game !");
    }

    private void normalRegister() {
        addonInstance = Skript.registerAddon(this);
        /*
         * TYPES
         */
        //Potions
        Classes.registerClass(new ClassInfo<>(PotionEffect.class, "potioneffect")
                .name("potion effect")
                .description("A getter for potion effects")
                .parser(new Parser<PotionEffect>() {
                            @Override
                            @Nullable
                            public PotionEffect parse(String obj, ParseContext context) {
                                return null;
                            }

                            @Override
                            public String toString(PotionEffect potionEffect, int i) {
                                return PotionEffectUtils.toString(potionEffect.getType()) + " of tier " + potionEffect.getAmplifier() + " lasting " + potionEffect.getDuration() + " with particles " + (potionEffect.hasParticles() ? "enabled" : "disabled") + ", ambient effect " + (potionEffect.isAmbient() ? "enabled" : "disabled");
                            }

                            @Override
                            public String toVariableNameString(PotionEffect potionEffect) {
                                return potionEffect.getType() + "," + potionEffect.getAmplifier() + "," + potionEffect.getDuration() + "," + potionEffect.hasParticles() + "," + potionEffect.isAmbient() + "," + potionEffect.getColor();
                            }

                            public String getVariableNamePattern() {
                                return ".+";
                            }
                        }
                )
        );
        //Banners
        Classes.registerClass(new ClassInfo<>(Pattern.class, "bannerlayer")
            .name("banner layer")
            .description("A getter for banner layers")
            .parser(new Parser<Pattern>() {
                @Override
                public Pattern parse(String s, ParseContext parseContext) {
                    return null;
                }

                @Override
                public String toString(Pattern pattern, int i) {
                    return "layer with pattern " + EnumType.toString(pattern.getPattern()) + " and color " + pattern.getColor().toString().toLowerCase();
                }

                @Override
                public String toVariableNameString(Pattern pattern) {
                    return "bannerlayer," + pattern.getPattern().toString().toLowerCase() + "," + pattern.getColor().toString().toLowerCase();
                }

                @Override
                public String getVariableNamePattern() {
                    return ".+";
                }
            })
        );
        EnumType.newType(PatternType.class, "bannerpattern", "banner ?pattern(?: ?type)?");
        //Syntax registration
        try {
            getAddon().loadClasses("fr.syst3ms.quarsk", "conditions",  "effects", "events", "expressions");
            getAddon().loadClasses("fr.syst3ms.quarsk.effects", "banner", "potion");
            getAddon().loadClasses("fr.syst3ms.quarsk.expressions", "banner", "beacon", "eventvalues", "potion");
        } catch (IOException e) {
            Skript.exception(e, "An error has occured while registering QuarSk's syntax", "Report this error to me, Syst3ms, and I will (hopefully) fix it");
        }
    }

    public void onDisable() {

    }

    public static <E extends Expression<T>, T> void newExpression(Class<E> clazz, Class<T> returnType, ExpressionType type, String... syntaxes) {
        Skript.registerExpression(clazz, returnType, type, prefixEachString(getSyntaxPrefix(), syntaxes));
        expressions.put(clazz, syntaxes);
    }

    public static <E extends Effect> void newEffect(Class<E> clazz, String... syntaxes) {
        Skript.registerEffect(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        effects.put(clazz, syntaxes);
    }

    public static <C extends Condition> void newCondition(Class<C> clazz, String... syntaxes) {
        Skript.registerCondition(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        conditions.put(clazz, syntaxes);
    }

    public static  <E extends SkriptEvent> void newEvent(String name, Class<E> skriptEvent, Class<? extends Event> event, String... syntaxes) {
        Skript.registerEvent(name, skriptEvent, event, syntaxes);
        events.put(skriptEvent, syntaxes);
    }

    private static boolean generateFolder() {
        if (!getPlugin().getDataFolder().exists()) {
            return getPlugin().getDataFolder().mkdirs();
        }
        return false;
    }

    private static boolean generateSyntaxFile() {
        File syntaxFile = new File(getPlugin(QuarSk.class).getDataFolder(), "syntax.txt");
        String newLine = System.getProperty("line.separator");
        if (!syntaxFile.exists()) {
            try {
                syntaxFile.createNewFile();
            } catch (IOException e) {
                return false;
            }
        } else {
            syntaxFile.delete();
        }
        BufferedWriter fileWriter;
        try {
            fileWriter = new BufferedWriter(new FileWriter(syntaxFile));
        } catch (IOException e) {
            return false;
        }
        try {
            writeLine(fileWriter, "Events :");
            for (Map.Entry<Class<?>, String[]> entry : events.entrySet()) {
                String syntaxName = StringUtils.space("(?<!^)(?=[A-Z])" , entry.getKey().getSimpleName().replaceAll("Skript", ""));
                for (String s : entry.getValue()) {
                    writeLine(fileWriter, "  - " + syntaxName + " : " + s);
                }
            }
            writeLine(fileWriter, "Conditions :");
            for (Map.Entry<Class<?>, String[]> entry : conditions.entrySet()) {
                String syntaxName = StringUtils.space("(?<!^)(?=[A-Z])" , entry.getKey().getSimpleName().replaceAll("^Cond", ""));
                for (String s : entry.getValue()) {
                    writeLine(fileWriter, "  - " + syntaxName + " : " + s);
                }
            }
            writeLine(fileWriter, "Effects :");
            for (Map.Entry<Class<?>, String[]> entry : effects.entrySet()) {
                String syntaxName = StringUtils.space("(?<!^)(?=[A-Z])" , entry.getKey().getSimpleName().replaceAll("^Eff", ""));
                for (String s : entry.getValue()) {
                    writeLine(fileWriter, "  - " + syntaxName + " : " + s);
                }
            }
            writeLine(fileWriter, "Expressions :");
            for (Map.Entry<Class<?>, String[]> entry : expressions.entrySet()) {
                String syntaxName = StringUtils.space("(?<!^)(?=[A-Z])" , entry.getKey().getSimpleName().replaceAll("^S?Expr", ""));
                for (String s : entry.getValue()) {
                    writeLine(fileWriter, "  - " + syntaxName + " : " + s);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                Skript.exception(e);
            }
        }
    }

    //Conventions
    public static String getPrefix() {
        return "[QuarSk] ";
    }

    public static void writeLine(BufferedWriter writer, String s) throws IOException {
        writer.write(s);
        writer.newLine();
    }

    public static SkriptAddon getAddon() {
        return addonInstance;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static String getSyntaxPrefix() {
        return "[quar[s]k] ";
    }

    public static String[] prefixEachString(String prefix, String... strings) {
        return Stream.of(strings).map(s -> prefix + s).toArray(String[]::new);
    }

    public static String[] suffixEachString(String suffix, String... strings) {
        return Stream.of(strings).map(s -> s + suffix).toArray(String[]::new);
    }

    public static String getVersion() {
        return "1.2";
    }

    //All of the below functions are by bi0qaw cause I honestly can't do that kind of math. Thanks to you.
    public static Vector vectorFromLocations(Location from, Location to) {
        return new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
    }

    public static float getYaw(Vector vector) {
        if (((Double) vector.getX()).equals((double) 0) && ((Double) vector.getZ()).equals((double) 0)) {
            return 0;
        }
        return (float) (Math.atan2(vector.getZ(), vector.getX()) * RAD_TO_DEG);
    }

    public static float getPitch(Vector vector) {
        double xy = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
        return (float) (Math.atan(vector.getY() / xy) * RAD_TO_DEG);
    }

    public static float notchYaw(float yaw) {
        float y = yaw - 90;
        if (y < -180) {
            y += 360;
        }
        return y;
    }

    public static float notchPitch(float pitch) {
        return -pitch;
    }

    public static void debug(Object... l) {
        Arrays.asList(l).forEach(s -> Bukkit.getLogger().log(Level.INFO, s.toString()));
    }

}

