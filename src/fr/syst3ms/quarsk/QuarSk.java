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
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.util.nms.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.TreeSpecies;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

@SuppressWarnings({"unused", "unchecked"})
public class Quarsk extends JavaPlugin {

    private static final double RAD_TO_DEG = 180 / Math.PI;
    private static SkriptAddon addonInstance;
    private static JavaPlugin plugin;
    private static Nms nms;

    public void onEnable() {
        if (getServer().getBukkitVersion().startsWith("1.7")) {
            getLogger().log(Level.INFO, "");
            getServer().getPluginManager().disablePlugin(this);
        }
        plugin = this;
        normalRegister();
        if (Registration.generateFolder())
            getLogger().log(Level.INFO, "Created QuarSk's folder !");
        if (Registration.generateSyntaxFile())
            getLogger().log(Level.INFO, "Generated QuarSk's syntax file !");
        getLogger().log(Level.INFO, "Registered " + Registration.getEvents().size() + " events, " + Registration.getConditions().size() + " conditions, " + Registration.getEffects().size() + " effects and " + Registration.getExpressions().size() + " expressions ! Good game !");
    }

    private void setupNms() {
        String version = getServer().getBukkitVersion();
        if (version.contains("1.8.8")) {
            getLogger().log(Level.INFO, "You're running 1.8.8 !");
            nms = new Nms_1_8_8();
        } else if (version.contains("1.9.4")) {
            getLogger().log(Level.INFO, "You're running 1.9.4 !");
            nms = new Nms_1_9_4();
        } else if (version.contains("1.10.2")) {
            getLogger().log(Level.INFO, "You're running 1.10.2 !");
            nms = new Nms_1_10_2();
        } else if (version.contains("1.11.2")) {
            getLogger().log(Level.INFO, "You're running 1.11.2 !");
            nms = new Nms_1_11_2();
        } else {
            getLogger().log(Level.WARNING, "You are running an unsupported version, so some features may not work. Supported versions : 1.8.8, 1.9.4, 1.10.2, 1.11.2");
        }
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
                                return potionEffect.getType().toString().toLowerCase() + "," + potionEffect.getAmplifier() + "," + potionEffect.getDuration() + "," + potionEffect.hasParticles() + "," + potionEffect.isAmbient() + "," + potionEffect.getColor().toString().toLowerCase();
                            }

                            public String getVariableNamePattern() {
                                return "[a-z]+,\\d+,\\d+,(true|false),(true|false),[a-z]+";
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
                    return "bannerlayer,[a-z]+,[a-z]+";
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

    //Conventions
    public static String getPrefix() {
        return "[Quarsk] ";
    }

    public static void writeLine(BufferedWriter writer, String s) throws IOException {
        writer.write(s);
        writer.newLine();
    }

    public static Nms getNms() {
        return nms;
    }

    public static SkriptAddon getAddon() {
        return addonInstance;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
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

    public static final class MinecraftVersions {
        public static final Version VERSION_1_8 = new Version(1, 8, 8);
        public static final Version VERSION_1_9 = new Version(1, 9, 4);
        public static final Version VERSION_1_10 = new Version(1, 10, 2);
        public static final Version VERSION_1_11 = new Version(1, 11, 2);
    }

}

