package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
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
public class ExprBeaconTier extends SimplePropertyExpression<Block, Number> {
	static {
		Registration.newPropertyExpression(
			ExprBeaconTier.class,
			Number.class,
			"beacon (tier|level)",
			"block"
		);
	}

	@Override
	protected String getPropertyName() {
		return "beacon tier";
	}

	@Override
	public Number convert(Block b) {
		if (b.getState() instanceof Beacon) {
			return ((Beacon) b.getState()).getTier();
		}
		return null;
	}

	@NotNull
	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}
}
