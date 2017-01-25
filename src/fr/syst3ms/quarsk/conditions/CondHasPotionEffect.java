package fr.syst3ms.quarsk.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by ARTHUR on 12/01/2017.
 */
public class CondHasPotionEffect extends Condition {
    private Expression<LivingEntity> entity;
    private Expression<PotionEffectType> type;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entity = (Expression<LivingEntity>) expr[0];
        type = (Expression<PotionEffectType>) expr[1];
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (entity.getSingle(e) != null && type.getSingle(e) != null) {
            return (entity.getSingle(e).hasPotionEffect(type.getSingle(e)));
        } else {
            return false;
        }
    }

    @Override
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }
}
