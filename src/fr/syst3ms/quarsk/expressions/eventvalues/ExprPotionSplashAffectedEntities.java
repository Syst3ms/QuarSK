package fr.syst3ms.quarsk.expressions.eventvalues;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PotionSplashEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprPotionSplashAffectedEntities extends SimpleExpression<LivingEntity> {

	static {
		Registration.newExpression(
			ExprPotionSplashAffectedEntities.class,
			LivingEntity.class,
			ExpressionType.SIMPLE,
			"[the] affected entities"
		);
	}

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		if (!ScriptLoader.isCurrentEvent(PotionSplashEvent.class)) {
			Skript.error(
				"The 'affected entities' expression can only be used in potion splash events",
				ErrorQuality.SEMANTIC_ERROR
			);
			return false;
		}
		return true;
	}

	@Override
	protected LivingEntity[] get(@NotNull Event e) {
		return ((PotionSplashEvent) e).getAffectedEntities().toArray(new LivingEntity[0]);
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
	public String toString(Event event, boolean b) {
		return "the affected entities";
	}
}
