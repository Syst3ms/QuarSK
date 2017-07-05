package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created by ARTHUR on 22/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprBannerOrShieldWithLayers extends SimpleExpression<ItemStack> {
	static {
		Registration.newExpression(
			ExprBannerOrShieldWithLayers.class,
			ItemStack.class,
			ExpressionType.COMBINED,
			"[new] (0¦banner|1¦shield) (with|using) [pattern[s]] %bannerlayers%"
		);
	}

	private Material mat;
	private Expression<Pattern> patterns;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
		mat = parseResult.mark == 0 ? Material.BANNER : Material.SHIELD;
		patterns = (Expression<Pattern>) expr[0];
		return true;
	}

	@NotNull
	@Override
	protected ItemStack[] get(Event e) {
		ItemStack returnItem = new ItemStack(mat);
		BannerMeta meta = ((BannerMeta) returnItem.getItemMeta());
		meta.setPatterns(Arrays.asList(patterns.getAll(e)));
		returnItem.setItemMeta(meta);
		return new ItemStack[]{returnItem};
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
		return mat.name().toLowerCase() + " with patterns " + patterns.toString(event, b);
	}
}
