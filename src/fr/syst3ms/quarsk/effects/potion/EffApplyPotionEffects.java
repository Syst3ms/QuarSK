package fr.syst3ms.quarsk.effects.potion;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by ARTHUR on 07/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class EffApplyPotionEffects extends Effect {
	static {
		Registration.newEffect(EffApplyPotionEffects.class, "apply %potioneffects% to %livingentities%");
	}

	private Expression<LivingEntity> targets;
	private Expression<PotionEffect> potionEffects;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		potionEffects = (Expression<PotionEffect>) expr[0];
		targets = (Expression<LivingEntity>) expr[1];
		return true;
	}

	@Override
	protected void execute(Event e) {
		if (potionEffects.getAll(e).length > 0 && targets.getAll(e).length > 0) {
			Stream.of(targets.getAll(e)).forEach(t -> t.addPotionEffects(Arrays.asList(potionEffects.getAll(e))));
		}
	}

	@NotNull
	@Override
	public String toString(Event e, boolean b) {
		return "apply " + potionEffects.toString(e, b) + " to " + targets.toString(e, b);
	}
}
