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
import fr.syst3ms.quarsk.util.apis.SpawnerApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

import java.util.OptionalInt;
import java.util.stream.Stream;

/**
 * Created by ARTHUR on 06/03/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprSingleSpawnerEntityType extends SimpleExpression<EntityType> {
    private Expression<Block> spawner;

    static {
        Registration.newDependantExpression("Spawner entity type",
                                    new String[]{"If the spawner is made to spawn multiple entity types, it will return the one that has the most chance of spawning",
                                                 "If there are multiple that have the most chance of spawning, it will pick a random one out of them",
                                                 "Resetting this expression will make the spawner spawn pigs (default entity type)"},
                                    SExprSingleSpawnerEntityType.class,
                                    EntityType.class,
                                    ExpressionType.PROPERTY,
                                    () -> !Bukkit.getPluginManager().isPluginEnabled("Skellett"),
                                    "spawned [entity] type of [spawner] %block%", "[spawner] %block%['s] spawned [entity] type");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        spawner = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected EntityType[] get(Event e) {
        if (spawner != null) {
            if (spawner.getSingle(e) != null) {
                if (spawner.getSingle(e).getType() == Material.MOB_SPAWNER) {
                    if (SpawnerApi.isSpawningSingleType(spawner.getSingle(e)))
                        return new EntityType[]{((CreatureSpawner) spawner.getSingle(e).getState()).getSpawnedType()};
                    else {
                        OptionalInt optionalInt = Stream.of(SpawnerApi.getSpawnedEntities(spawner.getSingle(e))).mapToInt(SpawnPotential::getWeight).distinct().max();
                        assert optionalInt.isPresent();
                        return Stream.of(SpawnerApi.getSpawnedEntities(spawner.getSingle(e)))
                                     .filter(pot -> pot.getWeight() == optionalInt.getAsInt())
                                     .map(SpawnPotential::getType)
                                     .toArray(EntityType[]::new);
                    }
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
                    assert mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET;
                    switch (mode) {
                        case SET:
                            assert delta[0] instanceof EntityType;
                            ((CreatureSpawner) spawner.getSingle(e).getState()).setSpawnedType((EntityType) delta[0]);
                            break;
                        case RESET:
                            ((CreatureSpawner) spawner.getSingle(e).getState()).setSpawnedType(EntityType.PIG);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET ?
                CollectionUtils.array(EntityType.class) : null;
    }

    @Override
    public Class<? extends EntityType> getReturnType() {
        return EntityType.class;
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
