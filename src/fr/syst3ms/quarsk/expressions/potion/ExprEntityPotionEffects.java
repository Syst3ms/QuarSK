package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * Created by ARTHUR on 07/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprEntityPotionEffects extends SimpleExpression<PotionEffect> {
	static {
		Registration.newExpression(
			ExprEntityPotionEffects.class,
			PotionEffect.class,
			ExpressionType.COMBINED,
			"[potion] effects (on|of) %livingentities%",
			"%livingentities%['s] [potion] effects"
		);
	}

	private Expression<LivingEntity> targets;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		targets = (Expression<LivingEntity>) expr[0];
		return true;
	}

	@Nullable
	@Override
	protected PotionEffect[] get(Event e) {
		if (targets.getArray(e).length > 0) {
			return Stream.of(targets.getAll(e))
						 .flatMap(le -> le.getActivePotionEffects().stream())
						 .toArray(PotionEffect[]::new);
		}
		return null;
	}

	@NotNull
	@Override
	public Class getReturnType() {
		return PotionEffect.class;
	}

	@NotNull
	@Override
	public String toString(Event e, boolean b) {
		return "potion effects on " + targets.toString(e, b);
	}

	@Override
	public boolean isSingle() {
		return false;
	}
}
