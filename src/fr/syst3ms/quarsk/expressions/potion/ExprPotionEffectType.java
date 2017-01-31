package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by ARTHUR on 08/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprPotionEffectType extends SimpleExpression<PotionEffectType> {
    private Expression<PotionEffect> effect;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        effect = (Expression<PotionEffect>) expr[0];
        return true;
    }

    @Override
    protected PotionEffectType[] get(Event e) {
        if (effect != null) {
            if (effect.getSingle(e) != null) {
                return new PotionEffectType[]{effect.getSingle(e).getType()};
            }
        }
        return null;
    }

    @Override
    public Class<? extends PotionEffectType> getReturnType() {
        return PotionEffectType.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }
}
