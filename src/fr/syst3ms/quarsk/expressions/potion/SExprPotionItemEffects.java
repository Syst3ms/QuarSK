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
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ARTHUR on 07/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprPotionItemEffects extends SimpleExpression<PotionEffect> {
	static {
		Registration.newExpression(
			SExprPotionItemEffects.class,
			PotionEffect.class,
			ExpressionType.COMBINED,
			"[potion] effects (on|of) %itemstack%",
			"%itemstack%['s] [potion] effects"
		);
	}

	private Expression<ItemStack> potionItem;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		potionItem = (Expression<ItemStack>) expr[0];
		return true;
	}

	@Nullable
	@Override
	protected PotionEffect[] get(Event e) {
		ItemStack i = potionItem.getSingle(e);
		if (i == null) {
			return null;
		}
		if (PotionUtils.isPotionItem(i)) {
			PotionMeta meta = (PotionMeta) i.getItemMeta();
			return PotionUtils.actualPotionEffects(meta);
		}
		return null;
	}

	@Override
	public void change(Event e, Object[] delta, @NotNull Changer.ChangeMode mode) {
		ItemStack i = potionItem.getSingle(e);
		if (i == null) {
			return;
		}
		if (i.getType() != Material.AIR) {
			if (i.getItemMeta() instanceof PotionMeta) {
				PotionMeta meta = ((PotionMeta) i.getItemMeta());
				switch (mode) {
					case ADD:
						for (PotionEffect eff : (PotionEffect[]) delta)
							meta.addCustomEffect(eff, true);
						i.setItemMeta(meta);
						break;
					case SET:
						meta.clearCustomEffects();
						meta.setBasePotionData(PotionUtils.emptyPotionData());
						for (PotionEffect eff : (PotionEffect[]) delta)
							meta.addCustomEffect(eff, true);
						i.setItemMeta(meta);
						break;
					case REMOVE:
					case REMOVE_ALL:
						for (PotionEffectType type : (PotionEffectType[]) delta)
							meta.removeCustomEffect(type);
						i.setItemMeta(meta);
						break;
					case DELETE:
					case RESET:
						meta.clearCustomEffects();
						meta.setBasePotionData(PotionUtils.emptyPotionData());
						i.setItemMeta(meta);
						break;
				}
			}
		}
	}

	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		if (mode != Changer.ChangeMode.REMOVE && mode != Changer.ChangeMode.REMOVE_ALL) {
			return CollectionUtils.array(PotionEffect[].class);
		} else {
			return CollectionUtils.array(PotionEffectType[].class);
		}
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@NotNull
	@Override
	public Class<? extends PotionEffect> getReturnType() {
		return PotionEffect.class;
	}

	@NotNull
	@Override
	public String toString(Event e, boolean b) {
		return "potion effects on " + potionItem.toString(e, b);
	}
}
