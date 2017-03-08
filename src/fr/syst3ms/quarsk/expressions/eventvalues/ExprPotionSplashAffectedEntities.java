package fr.syst3ms.quarsk.expressions.eventvalues;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PotionSplashEvent;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprPotionSplashAffectedEntities extends SimpleExpression<LivingEntity> {

    static {
        Registration.newExpression("Affected entities in the 'potion splash' event", ExprPotionSplashAffectedEntities.class, LivingEntity.class, ExpressionType.SIMPLE, "[the] affected entities");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected LivingEntity[] get(Event e) {
        if (e instanceof PotionSplashEvent) {
            return ((PotionSplashEvent) e).getAffectedEntities().stream().toArray(LivingEntity[]::new);
        }
        return null;
    }

    @Override
    public Class<? extends LivingEntity> getReturnType() {
        return LivingEntity.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
