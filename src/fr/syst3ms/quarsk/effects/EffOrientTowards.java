package fr.syst3ms.quarsk.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.QuarSk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

/**
 * Created by Syst3ms on 30/12/2016 in fr.syst3ms.quarsk.effects.
 */
@SuppressWarnings({"unused", "unchecked"})
public class EffOrientTowards extends Effect {
    private Expression<?> towards;
    private Expression<Entity> entity;
    private String mode;

    static {
        QuarSk.newEffect(EffOrientTowards.class, "orient %livingentity% (0¦towards|1¦away from) %location%", "make %livingentity% (face|look [at]) (0¦[towards]|1¦away from) %location%", "force %livingentity% to (face|look [at]) (0¦[towards]|1¦away from) %location%");
    }

    //A crap ton of ternary operators
    @Override
    protected void execute(Event e) {
        Entity ent = entity.getSingle(e);
        Vector velocity = ent.getVelocity();
        float fallDistance = ent.getFallDistance();
        Location from = ent.getLocation();
        Location to = (Location) towards.getSingle(e); //Getting the target location the location from the expression
        Vector direction = (mode.equals("toward")) ? QuarSk.vectorFromLocations(from, to) : QuarSk.vectorFromLocations(to, from); //Reversing the vector if the mdoe is "away"
        entity.getSingle(e).teleport(
                new Location(
                        ent.getWorld(),
                        ent.getLocation().getX(),
                        ent.getLocation().getY(),
                        ent.getLocation().getZ(),
                        QuarSk.notchYaw(QuarSk.getYaw(direction)),
                        QuarSk.notchPitch(QuarSk.getPitch(direction))
                )
        );
        ent.setVelocity(velocity);
        ent.setFallDistance(fallDistance);
    }

    @Override
    public String toString(Event e, boolean b) {
        return "(orient %entity%|(make %entity%|force %entity% to) (face|look)) (towards|away from) %location%";
    }

    //Here too
    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        mode = (parseResult.mark == 0) ? "toward" : "away";
        entity = (Expression<Entity>) expr[0];
        towards = expr[1];
        return true;
    }
}
