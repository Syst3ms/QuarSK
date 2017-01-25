package fr.syst3ms.quarsk.effects.potion;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;

/**
 * Created by ARTHUR on 12/01/2017.
 */
public class EffMilkEntity extends Effect {
    private Expression<LivingEntity> entity;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entity = (Expression<LivingEntity>) expr[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (entity.getAll(e) != null) {
            for (LivingEntity ent : entity.getAll(e)) {
                for (PotionEffect eff : ent.getActivePotionEffects()) {
                    ent.removePotionEffect(eff.getType());
                }
            }
        }
    }

    @Override
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }
}
