package fr.syst3ms.quarsk.expressions.eventvalues;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.QuarSk;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PotionSplashEvent;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprPotionSplashEventEntity extends SimpleExpression<Entity> {

    static {
        QuarSk.newExpression(ExprPotionSplashEventEntity.class, Entity.class, ExpressionType.SIMPLE, "[the] thrown potion [entity]");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected Entity[] get(Event e) {
        if (e instanceof PotionSplashEvent) {
            return new Entity[]{((PotionSplashEvent) e).getEntity()};
        }
        return null;
    }

    @Override
    public Class<? extends Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
