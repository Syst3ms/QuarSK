package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ARTHUR on 08/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprPotionEffectType extends SimplePropertyExpression<PotionEffect, PotionEffectType> {

	static {
		Registration.newPropertyExpression(ExprPotionEffectType.class,
			PotionEffectType.class,
			"[potion] effect type",
			"potioneffect"
		);
	}


	@Override
	public PotionEffectType convert(@NotNull PotionEffect potionEffect) {
		return potionEffect.getType();
	}

	@NotNull
	@Override
	protected String getPropertyName() {
		return "effect type";
	}

	@NotNull
	@Override
	public Class<? extends PotionEffectType> getReturnType() {
		return PotionEffectType.class;
	}
}
