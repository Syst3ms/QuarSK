package fr.syst3ms.quarsk.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.*;
import ch.njol.skript.util.StringMode;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Reference;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.event.Event;

public class EffLinkReference extends Effect {
	static {
		Registration.newEffect(EffLinkReference.class, "link @<.+?>@ to %object%");
	}

	private VariableString variableName;
	private Expression<?> value;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		VariableString s = VariableString.newInstance(parseResult.regexes.get(0).group(), StringMode.VARIABLE_NAME);
		if (s == null) {
			Skript.error("Invalid reference naming. A reference should be named like a variable");
			return false;
		}
		variableName = s;
		Expression<?> v = expressions[0];
		if (v instanceof UnparsedLiteral) {
			Skript.error("Unknown expression : " + ((UnparsedLiteral) v).getData());
			return false;
		}
		value = v;
		return true;
	}

	@Override
	protected void execute(Event event) {
		Reference.newInstance(variableName.toString(event), value, event);
	}

	@Override
	public String toString(Event event, boolean b) {
		return "link @" + ch.njol.util.StringUtils.substring(variableName.toString(event, b), 1, -1) + "@ to "
			   + value.toString(event, b);
	}
}
