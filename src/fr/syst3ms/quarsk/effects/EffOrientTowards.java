package fr.syst3ms.quarsk.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.util.MathUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Syst3ms on 30/12/2016 in fr.syst3ms.quarsk.effects.
 */
@SuppressWarnings({"unchecked"})
public class EffOrientTowards extends Effect {
	static {
		Registration.newEffect(
			EffOrientTowards.class,
			"orient %livingentity% (0¦towards|1¦away from) %location%",
			"make %livingentity% (face|look [at]) (0¦[towards]|1¦away from) %location%",
			"force %livingentity% to (face|look [at]) (0¦[towards]|1¦away from) %location%"
		);
	}

	private Expression<Location> towards;
	private Expression<Entity> entity;
	private boolean isToward;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
		isToward = parseResult.mark == 0;
		entity = (Expression<Entity>) expr[0];
		towards = (Expression<Location>) expr[1];
		return true;
	}

	@Override
	protected void execute(Event e) {
		Entity ent = entity.getSingle(e);
		Location to = towards.getSingle(e);
		if (ent == null || to == null) {
			return;
		}
		Vector velocity = ent.getVelocity();
		float fallDistance = ent.getFallDistance();
		Location from = ent.getLocation();
		Vector direction = isToward ? MathUtils.vectorFromLocations(from, to) : MathUtils.vectorFromLocations(to, from);
		ent.teleport(new Location(
			ent.getWorld(),
			from.getX(),
			from.getY(),
			from.getZ(),
			MathUtils.notchYaw(MathUtils.getYaw(direction)),
			MathUtils.notchPitch(MathUtils.getPitch(direction))
		));
		ent.setVelocity(velocity);
		ent.setFallDistance(fallDistance);
	}

	@NotNull
	@Override
	public String toString(Event e, boolean b) {
		return "orient " + entity.toString(e, b) + (isToward ? " towards " : " away from ") + towards.toString(e, b);
	}
}
