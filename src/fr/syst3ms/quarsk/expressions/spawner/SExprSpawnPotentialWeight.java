package fr.syst3ms.quarsk.expressions.spawner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.classes.SpawnPotential;
import org.bukkit.event.Event;

/**
 * Created by PRODSEB on 24/03/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprSpawnPotentialWeight extends SimpleExpression<Short> {
    private Expression<SpawnPotential> spawnPot;

    static {
        Registration.newExpression(
                "A spawn potential's weight",
                SExprSpawnPotentialWeight.class,
                Short.class,
                ExpressionType.PROPERTY,
                "weight of [spawn[ing] potential] %spawnpotential%", "[spawn[ing] potential] %spawnpotential%['s] weight"
        );
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        this.spawnPot = (Expression<SpawnPotential>) expr[0];
        return true;
    }

    @Override
    protected Short[] get(Event e) {
        if (spawnPot != null) {
            if (spawnPot.getSingle(e) != null) {
                return new Short[]{spawnPot.getSingle(e).getWeight()};
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (spawnPot != null) {
            if (spawnPot.getSingle(e) != null) {
                short newValue = (short) delta[0];
                switch (mode) {
                    case REMOVE:
                        spawnPot.getSingle(e).setWeight((short) (spawnPot.getSingle(e).getWeight() - newValue));
                        break;
                    case ADD:
                        spawnPot.getSingle(e).setWeight((short) (spawnPot.getSingle(e).getWeight() + newValue));
                        break;
                    case SET:
                        spawnPot.getSingle(e).setWeight(newValue);
                        break;
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.REMOVE
                ? CollectionUtils.array(Number.class) : null;
    }

    @Override
    public Class<? extends Short> getReturnType() {
        return Short.class;
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
