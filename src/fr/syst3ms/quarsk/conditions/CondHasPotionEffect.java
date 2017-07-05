package fr.syst3ms.quarsk.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ARTHUR on 12/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class CondHasPotionEffect extends Condition {
	static {
		Registration.newCondition(
			CondHasPotionEffect.class,
			"%livingentity% (0¦has [got]|1¦has( not|n't) [got]) [(the|a)] %potioneffecttype% [potion] [effect]"
		);
	}

	private Expression<LivingEntity> entity;
	private Expression<PotionEffectType> type;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
		entity = (Expression<LivingEntity>) expr[0];
		type = (Expression<PotionEffectType>) expr[1];
		setNegated(parseResult.mark == 1);
		return true;
	}

	@Override
	public boolean check(Event e) {
		return entity.check(e, livingEntity -> type.check(e, livingEntity::hasPotionEffect), isNegated());
	}

	@NotNull
	@Override
	public String toString(Event e, boolean b) {
		return entity.toString(e, b) + (isNegated() ? " hasn't " : " has ") + type.toString(e, b);
	}
}
