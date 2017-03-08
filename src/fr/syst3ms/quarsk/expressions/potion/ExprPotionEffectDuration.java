package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;

/**
 * Created by ARTHUR on 08/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprPotionEffectDuration extends SimpleExpression<Timespan> {
    private Expression<PotionEffect> effect;

    static {
        Registration.newExpression("Duration of a potion effect", ExprPotionEffectDuration.class, Timespan.class, ExpressionType.COMBINED, "(duration|length) of [potion] effect[s] %potioneffect%", "[potion] effect[s] %potioneffect%['s] (duration|length)");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        effect = (Expression<PotionEffect>) expr[0];
        return true;
    }

    @Override
    protected Timespan[] get(Event e) {
        if (effect.getSingle(e) != null) {
            return new Timespan[]{Timespan.fromTicks_i(effect.getSingle(e).getDuration())};
        } else {
            return null;
        }
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
    }

    @Override
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }

    @Override
    public boolean isSingle() {
        return false;
    }
}
