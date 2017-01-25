package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ARTHUR on 07/01/2017.
 */
public class ExprEntityPotionEffects extends SimpleExpression<PotionEffect> {
    private Expression<LivingEntity> targets;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        targets = (Expression<LivingEntity>) expr[0];
        return true;
    }

    @Override
    protected PotionEffect[] get(Event e) {
        if (targets != null) {
            if (targets.getArray(e).length > 0) {
                List<PotionEffect> effects = new ArrayList<>();
                for (LivingEntity ent : targets.getAll(e)) {
                    for (PotionEffect eff : ent.getActivePotionEffects()) {
                        effects.add(eff);
                    }
                }
                return effects.toArray(new PotionEffect[effects.size()]);
            } else {
                return null;
            }
        } else {
            return null;
        }
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
