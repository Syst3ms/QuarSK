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
import fr.syst3ms.quarsk.util.ListUtils;
import fr.syst3ms.quarsk.util.apis.SpawnerApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by PRODSEB on 12/03/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprSpawnerSpawnPotentials extends SimpleExpression<SpawnPotential> {
    private Expression<Block> spawner;

    static {
        Registration.newExpression("A spawner's spawn potentials", SExprSpawnerSpawnPotentials.class, SpawnPotential.class, ExpressionType.PROPERTY, "[all] spawn potentials of [spawner] %block%", "[spawner] %block%['s] spawn potentials");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        spawner = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected SpawnPotential[] get(Event e) {
        if (spawner != null) {
            if (spawner.getSingle(e) != null) {
                if (spawner.getSingle(e).getType() == Material.MOB_SPAWNER) {
                    return SpawnerApi.getSpawnedEntities(spawner.getSingle(e));
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
                    switch (mode) {
                        case SET:
                            SpawnerApi.setSpawnedEntities(spawner.getSingle(e), (SpawnPotential[]) delta);
                            break;
                        case ADD:
                            SpawnerApi.setSpawnedEntities(spawner.getSingle(e), ListUtils.flat(
                                    Arrays.asList(SpawnerApi.getSpawnedEntities(spawner.getSingle(e)),
                                                  (SpawnPotential[]) delta))
                                                      .stream()
                                                      .toArray(SpawnPotential[]::new)
                            );
                            break;
                        case REMOVE:
                            SpawnerApi.setSpawnedEntities(spawner.getSingle(e), Stream.of(SpawnerApi.getSpawnedEntities(spawner.getSingle(e)))
                                                                                      .filter(p -> p.getType() != delta[0])
                                                                                      .toArray(SpawnPotential[]::new)
                            );
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.ADD
                ? CollectionUtils.array(SpawnPotential[].class)
                : mode == Changer.ChangeMode.REMOVE ? CollectionUtils.array(EntityType.class) : null;
    }

    @Override
    public Class<? extends SpawnPotential> getReturnType() {
        return SpawnPotential.class;
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
