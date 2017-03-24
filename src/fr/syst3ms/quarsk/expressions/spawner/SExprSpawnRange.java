package fr.syst3ms.quarsk.expressions.spawner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.Math2;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.util.apis.SpawnerApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

/**
 * Created by PRODSEB on 15/03/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprSpawnRange extends SimpleExpression<Number> {
    private Expression<Block> spawner;

    static {
        Registration.newExpression(
                "A spawner's spawn range",
                SExprSpawnRange.class,
                Number.class,
                ExpressionType.PROPERTY,
                "spawn[ing] range of [spawner] %block%", "[spawner] %block%['s] spawn[ing] range"
        );
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        spawner = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected Number[] get(Event e) {
        if (spawner != null) {
            if (spawner.getSingle(e) != null) {
                if (spawner.getSingle(e).getType() == Material.MOB_SPAWNER) {
                    return new Number[]{SpawnerApi.getSpawnRange(spawner.getSingle(e))};
                }
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (spawner != null) {
            if (spawner.getSingle(e) != null) {
                if (spawner.getSingle(e).getType() == Material.MOB_SPAWNER) {
                    Number newValue = Math2.round((Float) delta[0]);
                    switch (mode) {
                        case SET:
                            SpawnerApi.setSpawnRange(spawner.getSingle(e), newValue.shortValue());
                            break;
                        case ADD:
                            SpawnerApi.setSpawnRange(spawner.getSingle(e), (short) (SpawnerApi.getSpawnRange(spawner.getSingle(e)) + newValue.shortValue()));
                            break;
                        case REMOVE:
                            SpawnerApi.setSpawnRange(spawner.getSingle(e), (short) (SpawnerApi.getSpawnRange(spawner.getSingle(e)) - newValue.shortValue()));;
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE
                ? CollectionUtils.array(Number.class) : null;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
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