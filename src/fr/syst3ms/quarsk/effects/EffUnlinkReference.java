package fr.syst3ms.quarsk.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.util.StringMode;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.Reference;
import org.bukkit.event.Event;

/**
 * Created by Raphaëlle on 01/01/2017.
 */
public class EffUnlinkReference extends Effect {
    private VariableString refName;

    @Override
    protected void execute(Event e) {
        String referenceName = refName.toString(e);
        if (Reference.referenceExists(referenceName)) {
            Reference.clear(referenceName);
        }
    }

    @Override
    public String toString(Event e, boolean b) {
        return "Reference unlink";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        VariableString varString = VariableString.newInstance(parseResult.regexes.get(0).group(), StringMode.VARIABLE_NAME);
        if (varString != null) {
            refName = varString;
        } else {
            Skript.error("Invalid reference name. References should be named the same way as variables.");
        }
        return (varString != null);
    }
}
