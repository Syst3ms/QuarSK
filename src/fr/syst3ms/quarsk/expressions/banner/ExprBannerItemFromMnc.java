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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by PRODSEB on 28/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprBannerItemFromMnc extends SimpleExpression<ItemStack> {
	static {
		Registration.newExpression(
			ExprBannerItemFromMnc.class,
			ItemStack.class,
			ExpressionType.COMBINED,
			"(0¦banner|1¦shield) from [m[iners]]n[eed]c[ool][s[hoes]] [code] %string%"
		);
	}

	private Material material;
	private Expression<String> mncCode;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
		material = (parseResult.mark == 0) ? Material.BANNER : Material.SHIELD;
		mncCode = (Expression<String>) expr[0];
		return true;
	}

	@Nullable
	@Override
	protected ItemStack[] get(Event e) {
		String code = mncCode.getSingle(e);
		if (code == null) {
			return null;
		}
		if (BannerUtils.isMncPattern(code)) {
			ItemStack item = new ItemStack(material);
			item.setItemMeta(BannerUtils.parseMncPattern(code));
			return new ItemStack[]{item};
		}
		return null;
	}

	@NotNull
	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return material.name().toLowerCase() + " from mnc code " + mncCode.toString(event, b);
	}
}
