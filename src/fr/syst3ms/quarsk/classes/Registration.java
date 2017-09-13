package fr.syst3ms.quarsk.classes;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.Classes;
import com.google.common.collect.ImmutableSet;
import fr.syst3ms.quarsk.QuarSk;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ARTHUR on 08/03/2017.
 */
public final class Registration {

	@NotNull
	private static Set<QuarskExpressionInfo> expressions = new HashSet<>();
	@NotNull
	private static Set<QuarskSyntaxInfo> effects = new HashSet<>();
	@NotNull
	private static Set<QuarskSyntaxInfo> conditions = new HashSet<>();
	@NotNull
	private static Set<QuarskSyntaxInfo> events = new HashSet<>();

	public static <E extends Expression<T>, T> void newExpression(@NotNull Class<E> clazz, @NotNull Class<T> returnType, @NotNull ExpressionType type, String... syntaxes) {
		Skript.registerExpression(clazz, returnType, type, prefixEachString(getSyntaxPrefix(), syntaxes));
		expressions.add(new QuarskExpressionInfo(returnType, syntaxes));
	}

	public static <E extends PropertyExpression<?, T>, T> void newPropertyExpression(@NotNull Class<E> clazz, @NotNull Class<T> returnType, String property, String type) {
		String first = String.format("[the] %s of %%%s%%", property, type);
		String second = String.format("%%%s%%'[s] %s", type, property);
		Skript.registerExpression(clazz, returnType, ExpressionType.PROPERTY, first, second);
		expressions.add(new QuarskExpressionInfo(returnType, first, second));
	}

	public static <E extends Effect> void newEffect(@NotNull Class<E> clazz, String... syntaxes) {
		Skript.registerEffect(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
		effects.add(new QuarskSyntaxInfo(syntaxes));
	}

	public static <C extends Condition> void newCondition(@NotNull Class<C> clazz, String... syntaxes) {
		Skript.registerCondition(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
		conditions.add(new QuarskSyntaxInfo(syntaxes));
	}

	public static <C extends PropertyCondition> void newPropertyCondition(@NotNull Class<C> clazz, String property, String type) {
		String first = String.format("%%%s%% (is|are) %s", type, property);
		String second = String.format("%%%s%% (isn't|is not|aren't|are not) %s", type, property);
		Skript.registerCondition(clazz, first, second);
		conditions.add(new QuarskSyntaxInfo(first, second));
	}

	public static <E extends SkriptEvent> void newEvent(String name, Class<E> skriptEvent, Class<? extends Event> event, String... syntaxes) {
		Skript.registerEvent(name, skriptEvent, event, prefixEachString(getSyntaxPrefix(), syntaxes));
		events.add(new QuarskSyntaxInfo(syntaxes));
	}

	public static String getSyntaxPrefix() {
		return "[quar[s]k] ";
	}

	public static String[] prefixEachString(String prefix, String... strings) {
		return Stream.of(strings).map(s -> prefix + s).toArray(String[]::new);
	}

	public static boolean generateFolder() {
		return !QuarSk.getPlugin().getDataFolder().exists() && QuarSk.getPlugin().getDataFolder().mkdirs();
	}

	public static boolean generateSyntaxFile() {
		File syntaxFile = new File(JavaPlugin.getPlugin(QuarSk.class).getDataFolder(), "syntax.txt");
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
			writeLine(fileWriter, "Notice : All syntax is inherently prefixed with '[quar[s]k]'");
			fileWriter.newLine();
			writeLine(fileWriter, "Events :");
			for (String s : events.stream().flatMap(i -> i.getPatterns().stream()).collect(Collectors.toList()))
				writeLine(fileWriter, "  - " + s);
			writeLine(fileWriter, "Conditions :");
			for (String s : conditions.stream().flatMap(i -> i.getPatterns().stream()).collect(Collectors.toList()))
				writeLine(fileWriter, "  - " + s);
			writeLine(fileWriter, "Effects :");
			for (String s : effects.stream().flatMap(i -> i.getPatterns().stream()).collect(Collectors.toList()))
				writeLine(fileWriter, "  - " + s);
			writeLine(fileWriter, "Expressions :");
			for (QuarskExpressionInfo info : expressions) {
				for (String s : info.getPatterns())
					writeLine(
						fileWriter,
						"  - " + s + " >> returns " + Classes.getExactClassInfo(info.getReturnType())
																  .getName()
																  .withIndefiniteArticle()
					);
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

	public static void writeLine(@NotNull BufferedWriter writer, @NotNull String s) throws IOException {
		writer.write(s);
		writer.newLine();
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

	public static class QuarskSyntaxInfo {
		private final List<String> patterns;

		public QuarskSyntaxInfo(String... patterns) {
			this.patterns = Stream.of(patterns)
								  .map(s -> s.replaceAll("(?<=%)(-[~*]|[~*])", "")
											 .replaceAll("(?<=[(|])\\d+?¦", "")
											 .replaceAll("@-?1(?=%)", ""))
								  .collect(Collectors.toList());
		}

		public List<String> getPatterns() {
			return patterns;
		}
	}

	public static class QuarskExpressionInfo extends QuarskSyntaxInfo {
		private final Class<?> returnType;

		public QuarskExpressionInfo(Class<?> returnType, String... patterns) {
			super(patterns);
			this.returnType = returnType;
		}

		public Class<?> getReturnType() {
			return returnType;
		}
	}
}
