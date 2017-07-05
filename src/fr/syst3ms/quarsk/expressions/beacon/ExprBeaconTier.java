package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Syst3ms on 16/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprBeaconTier extends SimpleExpression<Number> {
	static {
		Registration.newExpression(
			ExprBeaconTier.class,
			Number.class,
			ExpressionType.COMBINED,
			"beacon (tier|level) of %block%",
			"%block%['s] beacon (tier|level)"
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
	protected Number[] get(Event e) {
		Block b = beacon.getSingle(e);
		if (b == null) {
			return null;
		}
		if (b.getState() instanceof Beacon) {
			return new Number[]{((Beacon) b.getState()).getTier()};
		}
		return null;
	}

	@NotNull
	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return "beacon tier of " + beacon.toString(event, b);
	}

	@Override
	public boolean isSingle() {
		return true;
	}
}
