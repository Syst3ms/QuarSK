package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.Quarsk;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by ARTHUR on 06/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprCustomPotionEffect extends SimpleExpression<PotionEffect> {
    private Expression<PotionEffectType> type;
    private Expression<Timespan> duration;
    private Expression<Number> level;
    private Expression<Boolean> particles;
    private Expression<Boolean> ambient;
    private Expression<Color> color;

    static {
        Quarsk.newExpression("A custom potion effect", ExprCustomPotionEffect.class, PotionEffect.class, ExpressionType.COMBINED, "[[potion] effect [(with|by)]] %potioneffecttype% for %timespan% with [a] [tier [of]] %number% [particles %-boolean%[ with ambient [effect] %-boolean%[ and [particle] colo[u]r[ed] %-color%]]]]]");
    }

    @Override
    protected PotionEffect[] get(Event e) {
        PotionEffect effect =
                (color != null)
                        ? new PotionEffect(type.getSingle(e), duration.getSingle(e) != null ? Math.toIntExact((duration.getSingle(e).getTicks_i())) : 60, ((level != null) ? level.getSingle(e).intValue() : 1), ((ambient != null) ? ambient.getSingle(e) : false), ((particles != null) ? particles.getSingle(e) : true), (color.getSingle(e).getBukkitColor()))
                        : new PotionEffect(type.getSingle(e), duration.getSingle(e) != null ? Math.toIntExact((duration.getSingle(e).getTicks_i())) : 60, ((level != null) ? level.getSingle(e).intValue() : 1), ((ambient != null) ? ambient.getSingle(e) : false), ((particles != null) ? particles.getSingle(e) : true));
        return new PotionEffect[]{effect};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends PotionEffect> getReturnType() {
        return PotionEffect.class;
    }

    @Override
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        color = (Expression<Color>) expr[5];
        ambient = (Expression<Boolean>) expr[4];
        particles = (Expression<Boolean>) expr[3];
        level = (Expression<Number>) expr[2];
        duration = (Expression<Timespan>) expr[1];
        type = (Expression<PotionEffectType>) expr[0];
        return true;
    }
}
