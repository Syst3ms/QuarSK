package fr.syst3ms.quarsk.expressions.boat;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.Quarsk;
import org.bukkit.TreeSpecies;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

/**
 * Created by ARTHUR on 22/02/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprBoatTreeSpecie extends SimpleExpression<TreeSpecies> {
    private Expression<Entity> boatEntity;

    static {
        Quarsk.newVersionDependantExpression("A boatEntity's tree specie", SExprBoatTreeSpecie.class, TreeSpecies.class, ExpressionType.PROPERTY, Quarsk.MinecraftVersions.VERSION_1_9, "tree[ ]specie of [boatEntity] %entity%", "[boatEntity] %entity%['s] tree[ ]specie");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        boatEntity = (Expression<Entity>) expr[0];
        return true;
    }

    @Override
    protected TreeSpecies[] get(Event e) {
        if (boatEntity != null) {
            if (boatEntity.getSingle(e) != null) {
                if (boatEntity.getSingle(e).getType() == EntityType.BOAT) {
                    return new TreeSpecies[]{((Boat) boatEntity.getSingle(e)).getWoodType()};
                }
                Skript.error("The entity must be a boatEntity");
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (boatEntity != null) {
            if (boatEntity.getSingle(e) != null) {
                if (boatEntity.getSingle(e).getType() == EntityType.BOAT) {
                    assert mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET;
                    assert delta[0] instanceof TreeSpecies;
                    Boat boat = (Boat) boatEntity.getSingle(e);
                    switch (mode) {
                        case SET:
                            boat.setWoodType((TreeSpecies) delta[0]);
                            break;
                        case RESET:
                            boat.setWoodType(TreeSpecies.GENERIC);
                            break;
                    }
                }
                Skript.error("The entity must be a boatEntity");
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET)
            return CollectionUtils.array(TreeSpecies.class);
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends TreeSpecies> getReturnType() {
        return TreeSpecies.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
