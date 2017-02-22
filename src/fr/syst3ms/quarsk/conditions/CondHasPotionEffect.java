package fr.syst3ms.quarsk.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.Quarsk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by ARTHUR on 12/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class CondHasPotionEffect extends Condition {
    private Expression<LivingEntity> entity;
    private Expression<PotionEffectType> type;

    static {
        Quarsk.newCondition("Entity has potion effect condition", CondHasPotionEffect.class, "[entity] %livingentity% (0¦has [got]|1¦has( not|n't) [got]) [(the|a)] %potioneffecttype% [potion] effect");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entity = (Expression<LivingEntity>) expr[0];
        type = (Expression<PotionEffectType>) expr[1];
        setNegated(parseResult.mark == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (entity != null && type != null) {
            if (entity.getSingle(e) != null && type.getSingle(e) != null) {
                return isNegated() != entity.getSingle(e).hasPotionEffect(type.getSingle(e));
            }
        }
        return false;
    }

    @Override
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }
}
