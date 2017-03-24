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
import fr.syst3ms.quarsk.util.apis.SpawnerApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

/**
 * Created by PRODSEB on 13/03/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprMinMaxDelay extends SimpleExpression<Timespan> {
    private Expression<Block> spawner;
    private boolean isMin;

    static {
        Registration.newExpression("Mininum/Maximum delay of a spawner",
                                              new String[]{"If the spawner has a fixed delay, both of the expression's forms will return the same value"},
                                              SExprMinMaxDelay.class,
                                              Timespan.class, ExpressionType.PROPERTY,
                                    "(0¦min|1¦max)[imum] spawn delay of [spawner] %block%", "[spawner] %block%['s] (0¦min|1¦max)[imum] spawn delay");
    }

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        spawner = (Expression<Block>) expr[0];
        isMin = matchedPattern == 0;
        return true;
    }

    @Override
    protected Timespan[] get(Event e) {
        if (spawner != null) {
            if (spawner.getSingle(e) != null) {
                if (spawner.getSingle(e).getType() == Material.MOB_SPAWNER) {
                    return new Timespan[]{isMin ? SpawnerApi.getMinDelay(spawner.getSingle(e))
                            : SpawnerApi.getMaxDelay(spawner.getSingle(e))};
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
                            if (isMin)
                                SpawnerApi.setMinDelay(spawner.getSingle(e), new Timespan(SpawnerApi.getMinDelay(spawner.getSingle(e)).getMilliSeconds() + newValue.getMilliSeconds()));
                            SpawnerApi.setMaxDelay(spawner.getSingle(e), new Timespan(SpawnerApi.getMaxDelay(spawner.getSingle(e)).getMilliSeconds() + newValue.getMilliSeconds()));
                            break;
                        case REMOVE:
                            if (isMin)
                                SpawnerApi.setMinDelay(spawner.getSingle(e), new Timespan(SpawnerApi.getMinDelay(spawner.getSingle(e)).getMilliSeconds() - newValue.getMilliSeconds()));
                            SpawnerApi.setMaxDelay(spawner.getSingle(e), new Timespan(SpawnerApi.getMaxDelay(spawner.getSingle(e)).getMilliSeconds() - newValue.getMilliSeconds()));
                            break;
                        case SET:
                            if (isMin)
                                SpawnerApi.setMinDelay(spawner.getSingle(e), newValue);
                            SpawnerApi.setMaxDelay(spawner.getSingle(e), newValue);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.REMOVE
                ? CollectionUtils.array(Timespan.class) : null;
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
