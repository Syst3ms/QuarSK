package fr.syst3ms.quarsk.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.StringMode;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Reference;
import org.bukkit.event.Event;

import java.util.Optional;

public class SExprReference extends SimpleExpression<Object> {
	private VariableString variableName;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		VariableString v = VariableString.newInstance(parseResult.regexes.get(0).group(), StringMode.VARIABLE_NAME);
		if (v == null) {
			Skript.error("Invalid reference name.");
			return false;
		}
		return true;
	}

	@Override
	protected Object[] get(Event event) {
		String name = variableName.toString(event);
		Optional<Reference> optionalRef = Reference.byName(name);
		if (optionalRef.isPresent()) {
			Reference ref = optionalRef.get();
			return new Object[]{ref.getExpr().getSingle(ref.getEvent())};
		}
		return null;
	}

	@Override
	public Class<?> getReturnType() {
		if (variableName.isSimple()) {
			String simple = variableName.toString(null);
			Optional<Reference> optionalRef = Reference.byName(simple);
			if (optionalRef.isPresent()) {
				return optionalRef.get().getExpr().getReturnType();
			}
		}
		return Object.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public String toString(Event event, boolean b) {
		return "@" + ch.njol.util.StringUtils.substring(variableName.toString(event, b), 1, -1);
	}
}
