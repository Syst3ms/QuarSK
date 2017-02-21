package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.QuarSk;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;

/**
 * Created by ARTHUR on 08/01/2017.
 */
public class ExprPotionEffectTier extends SimpleExpression<Number> {
    private Expression<PotionEffect> effect;

    static {
        QuarSk.newExpression(ExprPotionEffectTier.class, Number.class, ExpressionType.COMBINED, "(tier|amplifier) of [potion] [effect] %potioneffect%", "[potion] [effect] %potioneffect%['s] (tier|amplifier)");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        effect = (Expression<PotionEffect>) expr[0];
        return true;
    }

    @Override
    protected Number[] get(Event e) {
       if (effect.getSingle(e) != null) {
           return new Number[]{effect.getSingle(e).getAmplifier()};
       } else {
           return null;
       }
    }


    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @Override
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }

    @Override
    public boolean isSingle() {
        return true;
    }
}
