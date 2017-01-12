package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by ARTHUR on 08/01/2017.
 */
public class ExprEffectTypeFromColor extends SimpleExpression {
    private Expression<Color> color;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        color = (Expression<Color>) expr[0];
        return true;
    }

    @Override
    protected Object[] get(Event e) {
        for (PotionEffectType type : PotionEffectType.values()) {
            if (type.getColor() == color.getSingle(e).getBukkitColor()) {
                return new PotionEffectType[]{type};
            }
        }
        return null;
    }

    @Override
    public Class getReturnType() {
        return PotionEffectType.class;
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
