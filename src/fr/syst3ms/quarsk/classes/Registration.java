package fr.syst3ms.quarsk.classes;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.*;
import com.google.common.collect.ImmutableSet;
import fr.syst3ms.quarsk.Quarsk;
import fr.syst3ms.quarsk.util.StringUtils;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ARTHUR on 08/03/2017.
 */
@SuppressWarnings("unused")
public final class Registration {

    private static Set<QuarskSyntaxInfo> expressions = new HashSet<>();
    private static Set<QuarskSyntaxInfo> effects = new HashSet<>();
    private static Set<QuarskSyntaxInfo> conditions = new HashSet<>();
    private static Set<QuarskSyntaxInfo> events = new HashSet<>();

    public static <E extends Expression<T>, T> void newExpression(String syntaxName, Class<E> clazz, Class<T> returnType, ExpressionType type, String... syntaxes) {
        Skript.registerExpression(clazz, returnType, type, prefixEachString(getSyntaxPrefix(), syntaxes));
        expressions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <E extends Expression<T>, T> void newExpression(String syntaxName, String[] description, Class<E> clazz, Class<T> returnType, ExpressionType type, String... syntaxes) {
        Skript.registerExpression(clazz, returnType, type, prefixEachString(getSyntaxPrefix(), syntaxes));
        expressions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <E extends Expression<T>, T> void newDependantExpression(String syntaxName, String[] description, Class<E> clazz, Class<T> returnType, ExpressionType type, BooleanSupplier condition, String... syntaxes) {
        if (condition.getAsBoolean()) {
            Skript.registerExpression(clazz, returnType, type, syntaxes);
            expressions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
        }
    }

    public static <E extends Effect> void newEffect(String syntaxName, Class<E> clazz, String... syntaxes) {
        Skript.registerEffect(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        effects.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <E extends Effect> void newEffect(String syntaxName, String[] description, Class<E> clazz, String... syntaxes) {
        Skript.registerEffect(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        effects.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <E extends Effect> void newDependantEffect(String syntaxName, String[] description, Class<E> clazz, BooleanSupplier condition, String... syntaxes) {
        if (condition.getAsBoolean()) {
            Skript.registerEffect(clazz, syntaxes);
            effects.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
        }
    }

    public static <C extends Condition> void newCondition(String syntaxName, Class<C> clazz, String... syntaxes) {
        Skript.registerCondition(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        conditions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <C extends Condition> void newCondition(String syntaxName, String[] description, Class<C> clazz, String... syntaxes) {
        Skript.registerCondition(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        conditions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
    }

    public static <C extends Condition> void newDependantCondition(String syntaxName, String[] description, Class<C> clazz, BooleanSupplier condition, String... syntaxes) {
        if (condition.getAsBoolean()) {
            Skript.registerCondition(clazz, syntaxes);
            conditions.add(new QuarskSyntaxInfo(clazz, syntaxName, syntaxes));
        }
    }

    public static <E extends SkriptEvent> void newEvent(String syntaxName, String name, Class<E> skriptEvent, Class<? extends Event> event, String... syntaxes) {
        Skript.registerEvent(name, skriptEvent, event, syntaxes);
        events.add(new QuarskSyntaxInfo(skriptEvent, syntaxName, syntaxes));
    }

    public static <E extends SkriptEvent> void newEvent(String syntaxName, String[] description, String name, Class<E> skriptEvent, Class<? extends Event> event, String... syntaxes) {
        Skript.registerEvent(name, skriptEvent, event, syntaxes);
        events.add(new QuarskSyntaxInfo(skriptEvent, syntaxName, syntaxes));
    }

    public static <E extends SkriptEvent, S extends Event> void newDependantEvent(String syntaxName, String[] description, String name, Class<E> skriptEvent, Class<S> event, BooleanSupplier condition, String... syntaxes) {
        if (condition.getAsBoolean()) {
            Skript.registerEvent(name, skriptEvent, event, syntaxes);
            events.add(new QuarskSyntaxInfo(skriptEvent, syntaxName, syntaxes));
        }
    }

    public static String getSyntaxPrefix() {
        return "[quar[s]k] ";
    }

    public static String[] prefixEachString(String prefix, String... strings) {
        return Stream.of(strings).map(s -> prefix + s).toArray(String[]::new);
    }

    public static boolean generateFolder() {
        if (!Quarsk.getPlugin().getDataFolder().exists()) {
            return Quarsk.getPlugin().getDataFolder().mkdirs();
        }
        return false;
    }

    public static boolean generateSyntaxFile() {
        File syntaxFile = new File(JavaPlugin.getPlugin(Quarsk.class).getDataFolder(), "syntax.txt");
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
            Quarsk.writeLine(fileWriter, "All syntax is inherently prefixed with '[quar[s]k]'");
            fileWriter.newLine();
            Quarsk.writeLine(fileWriter, "Events :");
            for (QuarskSyntaxInfo info : events) {
                for (String s : info.getPatterns())
                    Quarsk.writeLine(fileWriter, "  - " + info.getSyntaxName() + " : " + s);
                if (!info.getDescription().isEmpty()) {
                    for (String s : info.getDescription())
                        Quarsk.writeLine(fileWriter, "      - " + s);
                }
            }
            Quarsk.writeLine(fileWriter, "Conditions :");
            for (QuarskSyntaxInfo info : conditions) {
                for (String s : info.getPatterns())
                    Quarsk.writeLine(fileWriter, "  - " + info.getSyntaxName() + " : " + s);
                if (!info.getDescription().isEmpty()) {
                    for (String s : info.getDescription())
                        Quarsk.writeLine(fileWriter, "      - " + s);
                }
            }
            Quarsk.writeLine(fileWriter, "Effects :");
            for (QuarskSyntaxInfo info : effects) {
                for (String s : info.getPatterns())
                    Quarsk.writeLine(fileWriter, "  - " + info.getSyntaxName() + " : " + s);
                if (!info.getDescription().isEmpty()) {
                    for (String s : info.getDescription())
                        Quarsk.writeLine(fileWriter, "      - " + s);
                }
            }
            Quarsk.writeLine(fileWriter, "Expressions :");
            for (QuarskSyntaxInfo info : expressions) {
                for (String s : info.getPatterns())
                    Quarsk.writeLine(fileWriter, "  - " + info.getSyntaxName() + " : " + s);
                if (!info.getDescription().isEmpty()) {
                    for (String s : info.getDescription())
                        Quarsk.writeLine(fileWriter, "      - " + s);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                Skript.exception(e, "Honestly dunno how that happened");
            }
        }
    }

    public static class QuarskSyntaxInfo {
        private Class<?> clazz;
        private String syntaxName;
        private List<String> patterns = new ArrayList<>();
        private List<String> description;

        public QuarskSyntaxInfo(Class<?> clazz, String... patterns) {
            this(clazz, StringUtils.space("(?<!^)(?=[A-Z])" , clazz.getSimpleName().replaceAll("Skript", "")), patterns);
        }

        public QuarskSyntaxInfo(Class<?> clazz, String syntaxName, String... patterns) {
            this(clazz, syntaxName, new String[0], patterns);
        }

        public QuarskSyntaxInfo(Class<?> clazz, String syntaxName, String[] description, String... patterns) {
            this.clazz = clazz;
            this.syntaxName = syntaxName;
            this.patterns = Stream.of(patterns).map(s -> s.replaceAll("(?<=%)(-[~*]|[~*])", "").replaceAll("(?<=[(|])\\d+?¦", "").replaceAll("@-?1(?=%)", "")).collect(Collectors.toList());
            this.description = Arrays.asList(description);
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public String getSyntaxName() {
            return syntaxName;
        }

        public List<String> getPatterns() {
            return patterns;
        }

        public List<String> getDescription() {
            return description;
        }
    }

    public static Set<QuarskSyntaxInfo> getExpressions() {
        return ImmutableSet.copyOf(expressions);
    }

    public static Set<QuarskSyntaxInfo> getEffects() {
        return ImmutableSet.copyOf(effects);
    }

    public static Set<QuarskSyntaxInfo> getConditions() {
        return ImmutableSet.copyOf(conditions);
    }

    public static Set<QuarskSyntaxInfo> getEvents() {
        return ImmutableSet.copyOf(events);
    }
}
