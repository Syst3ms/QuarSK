package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ARTHUR on 06/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprCustomPotionEffect extends SimpleExpression<PotionEffect> {
	static {
		Registration.newExpression(ExprCustomPotionEffect.class,
			PotionEffect.class,
			ExpressionType.COMBINED,
			"%potioneffecttype% for %timespan% [with] tier %number% [particles %-boolean% [(ambient|beacon) %-boolean% [colo[u]r %-color%]]]]]"
		);
	}

	private Expression<PotionEffectType> type;
	private Expression<Timespan> duration;
	private Expression<Number> level;
	private Expression<Boolean> particles;
	private Expression<Boolean> ambient;
	private Expression<Color> color;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		type = (Expression<PotionEffectType>) expr[0];
		duration = (Expression<Timespan>) expr[1];
		level = (Expression<Number>) expr[2];
		particles = (Expression<Boolean>) expr[3];
		ambient = (Expression<Boolean>) expr[4];
		color = (Expression<Color>) expr[5];
		return true;
	}

	@Nullable
	@Override
	protected PotionEffect[] get(Event e) {
		PotionEffectType type = this.type.getSingle(e);
		Timespan duration = this.duration.getSingle(e);
		Number lvl = level.getSingle(e);
		Boolean amb = ambient.getSingle(e);
		Boolean part = particles.getSingle(e);
		Color c = color != null ? color.getSingle(e) : null;
		if (type == null || duration == null || lvl == null || amb == null || part == null || c == null) {
			return null;
		}
		PotionEffect effect;
		if (color != null) {
			effect = new PotionEffect(type,
				Math.toIntExact((duration.getTicks_i())),
				lvl.intValue(),
				amb,
				part,
				(c.getBukkitColor())
			);
		} else {
			effect = new PotionEffect(type, Math.toIntExact(duration.getTicks_i()), lvl.intValue(), amb, part);
		}
		return new PotionEffect[]{effect};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@NotNull
	@Override
	public Class<? extends PotionEffect> getReturnType() {
		return PotionEffect.class;
	}

	@Override
	public String toString(Event e, boolean b) {
		return type.toString(e, b) + " for " + duration.toString(e, b) + " tier " + level.toString(e, b) + (
			particles == null
				? ""
				: " particles " + particles.toString(e, b) + (ambient == null
					? ""
					: " ambient " + ambient.toString(e, b) + (color == null ? "" : " color " + color.toString(e, b))));
	}
}
