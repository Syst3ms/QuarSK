package fr.syst3ms.quarsk.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.util.StringMode;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.Quarsk;
import fr.syst3ms.quarsk.classes.Reference;
import org.bukkit.event.Event;

/**
 * Created by Syst3ms on 29/12/2016 in fr.syst3ms.quarsk.effects.
 */
public class EffLinkReference extends Effect {
    private VariableString refName;
    private Expression<?> exprToLink;

    static {
        Quarsk.newEffect("Links a reference to value", EffLinkReference.class, "link @<.+>@ to %object%");
    }

    @Override
    protected void execute(Event e) {
        String referenceName = refName.toString(e);
        boolean refExists = Reference.referenceExists(referenceName);
        if (refExists) {
            Reference ref = Reference.referenceByName(referenceName);
            ref.event = e;
            ref.expr = exprToLink;
        } else {
            Reference.newReference(referenceName, exprToLink, e);
        }
    }

    @Override
    public String toString(Event e, boolean b) {
        return "Reference linking effect. \n    Details :\n        - Reference Name : " + refName.toString(e, false) + "\n        - Expression to link : " + exprToLink.toString(e, false);
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        exprToLink = expr[0];
        refName = VariableString.newInstance(parseResult.regexes.get(0).group(), StringMode.VARIABLE_NAME);
        return (refName != null);
    }
}
