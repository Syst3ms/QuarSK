package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.util.PotionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Created by PRODSEB on 29/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprThrownPotionEffects extends SimpleExpression<PotionEffect> {
	static {
		Registration.newExpression(
			SExprThrownPotionEffects.class,
			PotionEffect.class,
			ExpressionType.COMBINED,
			"[all] [potion] effects (of|on) [(entity|thrown potion|tipped arrow)] %entity%"
		);
	}

	private Expression<Entity> entity;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		entity = (Expression<Entity>) expr[0];
		return true;
	}

	@Nullable
	@Override
	protected PotionEffect[] get(Event e) {
		Entity ent = entity.getSingle(e);
		if (ent == null) {
			return null;
		}
		if (ent instanceof ThrownPotion) {
			return ((ThrownPotion) ent).getEffects().stream().toArray(PotionEffect[]::new);
		} else if (ent instanceof TippedArrow) {
			return ((TippedArrow) ent).getCustomEffects().stream().toArray(PotionEffect[]::new);
		}
		return null;
	}

	@Override
	public void change(Event e, Object[] delta, @NotNull Changer.ChangeMode mode) {
		Entity ent = entity.getSingle(e);
		if (ent == null) {
			return;
		}
		if (PotionUtils.isEntityThrownPotion(ent)) {
			ItemStack item = ((ThrownPotion) ent).getItem();
			PotionMeta potionMeta = ((PotionMeta) ((ThrownPotion) ent).getItem().getItemMeta());
			switch (mode) {
				case ADD:
					Arrays.asList((PotionEffect[]) delta).forEach(eff -> potionMeta.addCustomEffect(eff, true));
					break;
				case SET:
					potionMeta.clearCustomEffects();
					Arrays.asList((PotionEffect[]) delta).forEach(eff -> potionMeta.addCustomEffect(eff, true));
					break;
				case REMOVE:
					potionMeta.removeCustomEffect((PotionEffectType) delta[0]);
					break;
				case DELETE:
					potionMeta.clearCustomEffects();
					break;
			}
			item.setItemMeta(potionMeta);
			((ThrownPotion) ent).setItem(item);
		} else if (ent.getType() == EntityType.TIPPED_ARROW) {
			TippedArrow tippedArrow = (TippedArrow) ent;
			switch (mode) {
				case ADD:
					Arrays.asList(((PotionEffect[]) delta)).forEach(eff -> tippedArrow.addCustomEffect(eff, true));
					break;
				case SET:
					tippedArrow.clearCustomEffects();
					Arrays.asList(((PotionEffect[]) delta)).forEach(eff -> tippedArrow.addCustomEffect(eff, true));
					break;
				case REMOVE:
					tippedArrow.removeCustomEffect((PotionEffectType) delta[0]);
					break;
				case DELETE:
					tippedArrow.clearCustomEffects();
					break;
			}
		}
	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		if (mode == Changer.ChangeMode.REMOVE) {
			return CollectionUtils.array(PotionEffectType.class);
		} else if (mode != Changer.ChangeMode.RESET) {
			return CollectionUtils.array(PotionEffect[].class);
		}
		return null;
	}

	@NotNull
	@Override
	public Class<? extends PotionEffect> getReturnType() {
		return PotionEffect.class;
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public String toString(Event event, boolean b) {
		return "potion effects on " + entity.toString(event, b);
	}
}
