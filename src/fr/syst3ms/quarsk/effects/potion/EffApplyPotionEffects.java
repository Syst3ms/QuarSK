package fr.syst3ms.quarsk.effects.potion;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.QuarSk;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;

/**
 * Created by ARTHUR on 07/01/2017.
 */
public class EffApplyPotionEffects extends Effect {
    private Expression<LivingEntity> player;
    private Expression<PotionEffect> potionEffects;

    @Override
    protected void execute(Event e) {
        if (potionEffects.getAll(e) != null && potionEffects.getAll(e).length > 0) {
            LivingEntity[] target = player.getAll(e);
            for (PotionEffect eff : potionEffects.getAll(e)) {
                for (LivingEntity ent : target) {
                    eff.apply(ent);
                }
            }
        }
    }

    @Override
    public String toString(Event e, boolean b) {
        return e.toString();
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        potionEffects = (Expression<PotionEffect>) expr[0];
        player = (Expression<LivingEntity>) expr[1];
        return true;
    }
}
