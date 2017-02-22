package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.Quarsk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;

import java.util.stream.Stream;

/**
 * Created by ARTHUR on 07/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprEntityPotionEffects extends SimpleExpression<PotionEffect> {
    private Expression<LivingEntity> targets;

    static {
        Quarsk.newExpression("All potion effects on an entity", ExprEntityPotionEffects.class, PotionEffect.class, ExpressionType.COMBINED, "[(all|every|each)] [active] [potion] effect[s] (on|in) %livingentities%", "[(all|each) of] %livingentities%['s] [active] [potion] effect[s]");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        targets = (Expression<LivingEntity>) expr[0];
        return true;
    }

    @Override
    protected PotionEffect[] get(Event e) {
        if (targets != null) {
            if (targets.getArray(e).length > 0) {
                return Stream.of(targets.getAll(e)).map(LivingEntity::getActivePotionEffects).toArray(PotionEffect[]::new);
            }
        }
        return null;
    }

    @Override
    public Class getReturnType() {
        return PotionEffect.class;
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
