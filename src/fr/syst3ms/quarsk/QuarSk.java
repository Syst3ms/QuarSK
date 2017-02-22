package fr.syst3ms.quarsk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.PotionEffectUtils;
import ch.njol.skript.util.Version;
import com.sun.istack.internal.Nullable;
import fr.syst3ms.quarsk.classes.EnumType;
import fr.syst3ms.quarsk.util.ListUtils;
import fr.syst3ms.quarsk.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TreeSpecies;
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
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "unchecked"})
public class Quarsk extends JavaPlugin {

    private static final double RAD_TO_DEG = 180 / Math.PI;
    private static SkriptAddon addonInstance;
    private static Set<QuarskSyntaxInfo> expressions = new HashSet<>();
    private static Set<QuarskSyntaxInfo> effects = new HashSet<>();
    private static Set<QuarskSyntaxInfo> conditions = new HashSet<>();
    private static Set<QuarskSyntaxInfo> events = new HashSet<>();
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
        EnumType.newType(TreeSpecies.class, "treespecie", "(?:tree|wood) ?specie");
        //Syntax registration
        try {
            getAddon().loadClasses("fr.syst3ms.quarsk", "conditions",  "effects", "events", "expressions");
            getAddon().loadClasses("fr.syst3ms.quarsk.effects", "banner", "potion");
            getAddon().loadClasses("fr.syst3ms.quarsk.expressions", "banner", "beacon", "eventvalues", "potion");
        } catch (IOException e) {
            Skript.exception(e, "An error has occured while registering Quarsk's syntax", "Report this error to me, Syst3ms, and I will (hopefully) fix it");
        }
    }

    public void onDisable() {

    }

    public static <E extends Expression<T>, T> void newExpression(String syntaxName, Class<E> clazz, Class<T> returnType, ExpressionType type, String... syntaxes) {
        Skript.registerExpression(clazz, returnType, type, prefixEachString(getSyntaxPrefix(), syntaxes));
        expressions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <E extends Expression<T>, T> void newVersionDependantExpression(String syntaxName, Class<E> clazz, Class<T> returnType, ExpressionType type, Version version, String... syntaxes) {
        if (Skript.isRunningMinecraft(version)) {
            Skript.registerExpression(clazz, returnType, type, syntaxes);
            expressions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
        }
    }

    public static <E extends Effect> void newEffect(String syntaxName, Class<E> clazz, String... syntaxes) {
        Skript.registerEffect(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        effects.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <E extends Effect> void newVersionDependantEffect(String syntaxName, Class<E> clazz, Version version, String... syntaxes) {
        if (Skript.isRunningMinecraft(version)) {
            Skript.registerEffect(clazz, syntaxes);
            effects.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
        }
    }

    public static <C extends Condition> void newCondition(String syntaxName, Class<C> clazz, String... syntaxes) {
        Skript.registerCondition(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        conditions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <C extends Condition> void newVersionDependantCondition(String syntaxName, Class<C> clazz, Version version, String... syntaxes) {
        if (Skript.isRunningMinecraft(version)) {
            Skript.registerCondition(clazz, syntaxes);
            conditions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
        }
    }

    public static <E extends SkriptEvent> void newEvent(String syntaxName, String name, Class<E> skriptEvent, Class<? extends Event> event, String... syntaxes) {
        Skript.registerEvent(name, skriptEvent, event, syntaxes);
        events.add(new QuarskSyntaxInfo(skriptEvent, syntaxName, syntaxes));
    }

    public static <E extends SkriptEvent, S extends Event> void newVersionDependantEvent(String syntaxName, String name, Class<E> skriptEvent, Class<S> event, Version version, String... syntaxes) {
        if (Skript.isRunningMinecraft(version)) {
            Skript.registerEvent(name, skriptEvent, event, syntaxes);
            events.add(new QuarskSyntaxInfo(skriptEvent, syntaxName, syntaxes));
        }
    }



    private static boolean generateFolder() {
        if (!getPlugin().getDataFolder().exists()) {
            return getPlugin().getDataFolder().mkdirs();
        }
        return false;
    }

    private static boolean generateSyntaxFile() {
        File syntaxFile = new File(getPlugin(Quarsk.class).getDataFolder(), "syntax.txt");
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
            for (QuarskSyntaxInfo info : events) {
                for (String s : info.getPatterns()) {
                    writeLine(fileWriter, "  - " + info.getSyntaxName() + " : " + s);
                }
            }
            writeLine(fileWriter, "Conditions :");
            for (QuarskSyntaxInfo info : conditions) {
                for (String s : info.getPatterns()) {
                    writeLine(fileWriter, "  - " + info.getSyntaxName() + " : " + s);
                }
            }
            writeLine(fileWriter, "Effects :");
            for (QuarskSyntaxInfo info : effects) {
                for (String s : info.getPatterns()) {
                    writeLine(fileWriter, "  - " + info.getSyntaxName() + " : " + s);
                }
            }
            writeLine(fileWriter, "Expressions :");
            for (QuarskSyntaxInfo info : expressions) {
                for (String s : info.getPatterns()) {
                    writeLine(fileWriter, "  - " + info.getSyntaxName() + " : " + s);
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
        return "[Quarsk] ";
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

    private static class QuarskSyntaxInfo {
        private Class<?> clazz;
        private String syntaxName;
        private List<String> patterns = new ArrayList<>();

        public QuarskSyntaxInfo(Class<?> clazz, String... patterns) {
            this(clazz, StringUtils.space("(?<!^)(?=[A-Z])" , clazz.getSimpleName().replaceAll("Skript", "")), patterns);
        }

        public QuarskSyntaxInfo(Class<?> clazz, String syntaxName, String... patterns) {
            this.clazz = clazz;
            this.syntaxName = syntaxName;
            this.patterns = ListUtils.mapList(Arrays.asList(patterns), s -> s.replaceAll("(?<=%)(-[~*]|[~*])", "").replaceAll("(?<=[(|])\\d+?¦", "").replaceAll("@-?1(?=%)", ""));
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public String getSyntaxName() {
            return syntaxName;
        }

        public String[] getPatterns() {
            return patterns.toArray(new String[patterns.size()]);
        }
    }

    public static final class MinecraftVersions {
        public static final Version VERSION_1_8 = new Version(1, 8, 8);
        public static final Version VERSION_1_9 = new Version(1, 9, 4);
        public static final Version VERSION_1_10 = new Version(1, 10, 2);
        public static final Version VERSION_1_11 = new Version(1, 11, 2);
    }

}

