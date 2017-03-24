package fr.syst3ms.quarsk.effects.spawner;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.util.apis.SpawnerApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

/**
 * Created by PRODSEB on 15/03/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class EffMakeSpawnEntity extends Effect {
    private Expression<Block> spawner;
    private Expression<Entity> entity;

    static {
        Registration.newEffect(
                "Makes a spawner spawn an entity with all of its data",
                EffMakeSpawnEntity.class,
                "make [spawner] %block% spawn %entity%", "force [spawner] %block% to spawn %entity%"
        );
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        spawner = (Expression<Block>) expr[0];
        entity = (Expression<Entity>) expr[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (spawner != null && entity != null) {
            if (spawner.getSingle(e) != null && entity.getSingle(e) != null) {
                if (spawner.getSingle(e).getType() == Material.MOB_SPAWNER) {
                    SpawnerApi.setSingleSpawnedEntity(spawner.getSingle(e), entity.getSingle(e));
                }
            }
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
