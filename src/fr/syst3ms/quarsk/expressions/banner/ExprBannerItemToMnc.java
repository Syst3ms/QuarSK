package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.util.BannerUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ARTHUR on 03/02/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprBannerItemToMnc extends SimpleExpression<String> {
	static {
		Registration.newExpression(
			ExprBannerItemToMnc.class,
			String.class,
			ExpressionType.COMBINED,
			"[m[iners]]n[eed]c[ool][s[hoes]] code of %itemstack%",
			"%itemstack%['s] [m[iners]]n[eed]c[ool][s[hoes]] code"
		);
	}

	private Expression<ItemStack> item;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		item = (Expression<ItemStack>) expr[0];
		return true;
	}

	@Nullable
	@Override
	protected String[] get(Event e) {
		ItemStack i = item.getSingle(e);
		if (i == null) {
			return null;
		}
		if (i.getType() == Material.BANNER || i.getType() == Material.SHIELD) {
			return new String[]{BannerUtils.toMncPattern((BannerMeta) i.getItemMeta())};
		}
		return null;
	}

	@NotNull
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return "mnc code of " + item.toString(event, b);
	}
}
