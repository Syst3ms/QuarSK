package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

/**
 * Created by Syst3ms on 16/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprBeaconTier extends SimpleExpression<Number> {
    private Expression<Block> beacon;

    static {
        Registration.newExpression("Tier of a beacon", ExprBeaconTier.class, Number.class, ExpressionType.COMBINED, "beacon (tier|level) of %block%", "%block%['s] beacon (tier|level)");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        beacon = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected Number[] get(Event e) {
        if (beacon.getSingle(e) != null) {
            if (beacon.getSingle(e).getState() instanceof Beacon) {
                return new Number[]{((Beacon) beacon.getSingle(e).getState()).getTier()};
            }
        }
        return null;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }

    @Override
    public boolean isSingle() {
        return true;
    }
}
