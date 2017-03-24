package fr.syst3ms.quarsk.expressions.spawner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.classes.SpawnPotential;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

/**
 * Created by PRODSEB on 17/03/2017.
 */
@SuppressWarnings("all")
public class ExprEntitySpawnPotential extends SimpleExpression<SpawnPotential> {
    private Expression<Entity> entity;

    static {
        Registration.newExpression(
                "Spawn potential from entity",
                ExprEntitySpawnPotential.class,
                SpawnPotential.class,
                ExpressionType.COMBINED,
                "spawn[ing] potential from [entity] %entity%"
        );
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entity = (Expression<Entity>) expr[0];
        return true;
    }

    @Override
    protected SpawnPotential[] get(Event e) {
        if (entity != null) {
            if (entity.getSingle(e) != null) {
                return new SpawnPotential[]{
                        new SpawnPotential(entity.getSingle(e))
                };
            }
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends SpawnPotential> getReturnType() {
        return SpawnPotential.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
