package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Timespan;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ARTHUR on 08/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprPotionEffectDuration extends SimplePropertyExpression<PotionEffect, Timespan> {

	static {
		Registration.newPropertyExpression(ExprPotionEffectDuration.class, Timespan.class, "duration", "potioneffect");
	}

	@NotNull
	@Override
	public Timespan convert(@NotNull PotionEffect potionEffect) {
		return Timespan.fromTicks_i(potionEffect.getDuration());
	}

	@NotNull
	@Override
	protected String getPropertyName() {
		return "duration";
	}

	@NotNull
	@Override
	public Class<? extends Timespan> getReturnType() {
		return Timespan.class;
	}
}
