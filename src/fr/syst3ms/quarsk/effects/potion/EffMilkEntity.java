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

/**
 * Created by ARTHUR on 12/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class EffMilkEntity extends Effect {
	static {
		Registration.newEffect(EffMilkEntity.class, "milk %livingentities%");
	}

	private Expression<LivingEntity> entities;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		entities = (Expression<LivingEntity>) expr[0];
		return true;
	}

	@Override
	protected void execute(Event e) {
		for (LivingEntity ent : entities.getAll(e)) {
			for (PotionEffect eff : ent.getActivePotionEffects()) {
				ent.removePotionEffect(eff.getType());
			}
		}
	}

	@NotNull
	@Override
	public String toString(Event e, boolean b) {
		return "milk " + entities.toString(e, b);
	}
}
