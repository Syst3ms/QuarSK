package fr.syst3ms.quarsk.expressions.eventvalues;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PotionSplashEvent;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprPotionSplashEventEntity extends SimpleExpression<Entity> {

    static {
        Registration.newExpression("Thrown potion in the 'potion splash' event", ExprPotionSplashEventEntity.class, Entity.class, ExpressionType.SIMPLE, "[the] thrown potion [entity]");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        if (!ScriptLoader.isCurrentEvent(PotionSplashEvent.class)) {
            Skript.error("The 'thrown potion' expression can only be used in potion splash events", ErrorQuality.SEMANTIC_ERROR);
			return false;    
        }
        return true;
    }

    @Override
    protected Entity[] get(Event e) {
        return new Entity[]{((PotionSplashEvent) e).getEntity()};
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
