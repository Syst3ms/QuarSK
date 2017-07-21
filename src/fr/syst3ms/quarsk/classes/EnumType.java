package fr.syst3ms.quarsk.classes;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnumType {

	public static <T extends Enum<T>> void newType(@NotNull final Class<T> c, @NotNull String code, String regexUser) {
		Classes.registerClass(new ClassInfo<>(c, code).user(regexUser)
													  .name(c.getSimpleName())
													  .defaultExpression(new EventValueExpression<>(c))
													  .parser(new Parser<T>() {

														  @Override
														  @Nullable
														  public T parse(String name, ParseContext arg1) {
															  name = fromString(name);
															  if (name.startsWith(
																  c.getSimpleName().toUpperCase() + ".")) {
																  name = name.split("\\.")[1];
															  }
															  try {
																  return Enum.valueOf(c, name);
															  } catch (Exception ignored) {
															  }
															  return null;
														  }

														  @Override
														  public String toString(@NotNull T e, int arg1) {
															  return EnumType.toString(e.name());
														  }

														  @NotNull
														  @Override
														  public String toVariableNameString(@NotNull T e) {
															  return c.getSimpleName() + "." + e.name();
														  }

														  @NotNull
														  @Override
														  public String getVariableNamePattern() {
															  return "(" + c.getSimpleName().toLowerCase() + "\\.)?.+";
														  }

													  }));
	}

	public static String toString(@NotNull String s) {
		return s.toLowerCase().replaceAll("_", " ");
	}

	public static String fromString(@NotNull String str) {
		return str.toUpperCase().replaceAll("\\s+", "_");
	}
}