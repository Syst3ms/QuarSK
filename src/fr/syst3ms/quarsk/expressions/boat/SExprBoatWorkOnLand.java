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
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

/**
 * Created by ARTHUR on 22/02/2017.
 */
@SuppressWarnings({"unused", "unchecked", "deprecation"})
public class SExprBoatWorkOnLand extends SimpleExpression<Boolean> {
    private Expression<Entity> boatEntity;

    static {
        Quarsk.newExpression("Whether the boat can work when not on water", SExprBoatWorkOnLand.class, Boolean.class, ExpressionType.PROPERTY, "[the] work on (land|ground) state of [boat] %entity%", "[boat] %entity%['s] work on (ground|land) state");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        boatEntity = (Expression<Entity>) expr[0];
        return true;
    }

    @Override
    protected Boolean[] get(Event e) {
        if (boatEntity != null) {
            if (boatEntity.getSingle(e) != null) {
                if (boatEntity.getSingle(e).getType() == EntityType.BOAT) {
                    return new Boolean[]{((Boat) boatEntity.getSingle(e)).getWorkOnLand()};
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
                    assert delta[0] instanceof Boolean;
                    Boat boat = (Boat) boatEntity.getSingle(e);
                    switch (mode) {
                        case SET:
                            boat.setWorkOnLand((Boolean) delta[0]);
                            break;
                        case RESET:
                            boat.setWorkOnLand(Boolean.FALSE);
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
            return CollectionUtils.array(Boolean.class);
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
