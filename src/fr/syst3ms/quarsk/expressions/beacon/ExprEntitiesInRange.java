package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;

/**
 * Created by Syst3ms on 15/01/2017.
 */
public class ExprEntitiesInRange extends SimpleExpression<LivingEntity> {
    private Expression<Block> beacon;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        beacon = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected LivingEntity[] get(Event e) {
        if (beacon.getSingle(e) != null) {
            if (beacon.getSingle(e).getType() == Material.BEACON) {
                Beacon state = ((Beacon) beacon.getSingle(e).getState());
                return state.getEntitiesInRange().toArray(new LivingEntity[state.getEntitiesInRange().size()]);
            }
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
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }
}
