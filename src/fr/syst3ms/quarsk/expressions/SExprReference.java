package fr.syst3ms.quarsk.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.StringMode;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Reference;
import org.bukkit.event.Event;

/**
 * Created by Syst3ms on 29/12/2016 in fr.syst3ms.quarsk.expressions.
 */
public class SExprReference extends SimpleExpression {
    private VariableString name;
    private Reference refProp;

    @Override
    protected Object[] get(Event e) {
        String stringName = name.toString(e);
        Reference ref = Reference.referenceByName(stringName);
        if (ref != null) {
            refProp = ref;
            return new Object[]{ref.getValue()};
        } else {
            return null;
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class getReturnType() {
        return Object.class;
    }

    public Class<?>[] acceptChange(Changer.ChangeMode changeMode) {
        return CollectionUtils.array(Object.class);
    }

    public void change(Event e, Object[] o, Changer.ChangeMode changeMode) {
        if (this.acceptChange(changeMode) != null) {
            refProp.expr.change(e, o, changeMode);
        }
    }

    @Override
    public String toString(Event e, boolean b) {
        return this.get(e).toString();
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        VariableString varString = VariableString.newInstance(parseResult.regexes.get(0).group(), StringMode.VARIABLE_NAME);
        if (varString != null) {
            name = varString;
        } else {
            Skript.error("Invalid reference name. Reference names should be written the same way as variable names.");
        }
        return (varString != null);
    }
}
