package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Syst3ms on 15/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprEntitiesInRange extends SimpleExpression<LivingEntity> {
	static {
		Registration.newExpression(
			ExprEntitiesInRange.class,
			LivingEntity.class,
			ExpressionType.COMBINED,
			"entities in range of [beacon] %block%"
		);
	}

	private Expression<Block> beacon;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		beacon = (Expression<Block>) expr[0];
		return true;
	}

	@Nullable
	@Override
	protected LivingEntity[] get(Event e) {
		Block b = beacon.getSingle(e);
		if (b == null) {
			return null;
		}
		if (b.getType() == Material.BEACON) {
			Beacon state = (Beacon) b.getState();
			return state.getEntitiesInRange().toArray(new LivingEntity[state.getEntitiesInRange().size()]);
		}
		return null;
	}

	@NotNull
	@Override
	public Class<? extends LivingEntity> getReturnType() {
		return LivingEntity.class;
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@NotNull
	@Override
	public String toString(Event e, boolean b) {
		return "entities in range of " + beacon.toString(e, b);
	}
}
