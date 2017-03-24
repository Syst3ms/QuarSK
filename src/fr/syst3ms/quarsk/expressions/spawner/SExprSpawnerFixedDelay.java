package fr.syst3ms.quarsk.expressions.spawner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.classes.SpawnPotential;
import fr.syst3ms.quarsk.util.apis.SpawnerApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

/**
 * Created by ARTHUR on 08/03/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprSpawnerFixedDelay extends SimpleExpression<Timespan> {
    private Expression<Block> spawner;

    static {
        Registration.newDependantExpression(
                "A spawner's fixed delay",
                new String[]{"If the spawner has a min and max delay, it will return a mean of the two"},
                SExprSpawnerFixedDelay.class,
                Timespan.class,
                ExpressionType.PROPERTY,
                () -> !Bukkit.getPluginManager().isPluginEnabled("Skellett"),
                "[fixed] spawn delay of [spawner] %block%", "[spawner] %block%['s] [fixed] spawn delay"
        );
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        spawner = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected Timespan[] get(Event e) {
        if (spawner != null) {
            if (spawner.getSingle(e) != null) {
                if (spawner.getSingle(e).getType() == Material.MOB_SPAWNER) {
                    return new Timespan[]{SpawnerApi.getFixedDelay(spawner.getSingle(e))};
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
                    Timespan newValue = (Timespan) delta[0];
                    switch (mode) {
                        case ADD:
                            SpawnerApi.setFixedDelay(spawner.getSingle(e), new Timespan(SpawnerApi.getFixedDelay(spawner.getSingle(e)).getMilliSeconds() + newValue.getMilliSeconds()));
                            break;
                        case REMOVE:
                            SpawnerApi.setFixedDelay(spawner.getSingle(e), new Timespan(SpawnerApi.getFixedDelay(spawner.getSingle(e)).getMilliSeconds() - newValue.getMilliSeconds()));
                            break;
                        case SET:
                            SpawnerApi.setFixedDelay(spawner.getSingle(e), newValue);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE ?
                CollectionUtils.array(Timespan.class) : null;
    }

    @Override
    public Class<? extends Timespan> getReturnType() {
        return Timespan.class;
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