package fr.syst3ms.quarsk.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.util.StringMode;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Reference;
import org.bukkit.event.Event;

public class EffUnlinkReference extends Effect {
	private VariableString variableName;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		VariableString v = VariableString.newInstance(parseResult.regexes.get(0).group(), StringMode.VARIABLE_NAME);
		if (v == null) {
			Skript.error("Invalid reference name.");
			return false;
		}
		variableName = v;
		return true;
	}

	@Override
	protected void execute(Event event) {
		Reference.removeReference(variableName.toString(event));
	}

	@Override
	public String toString(Event event, boolean b) {
		return "unlink @" + ch.njol.util.StringUtils.substring(variableName.toString(event, b), 1, -1) + "@";
	}
}
