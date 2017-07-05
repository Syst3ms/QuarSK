package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ARTHUR on 08/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprPotionEffectTier extends SimplePropertyExpression<PotionEffect, Number> {

	static {
		Registration.newPropertyExpression(ExprPotionEffectTier.class, Number.class, "tier", "potioneffect");
	}

	@Override
	public Number convert(PotionEffect potionEffect) {
		return potionEffect.getAmplifier();
	}

	@Override
	protected String getPropertyName() {
		return "tier";
	}

	@NotNull
	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}
}
