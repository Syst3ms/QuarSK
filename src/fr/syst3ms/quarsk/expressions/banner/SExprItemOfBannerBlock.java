package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprItemOfBannerBlock extends SimpleExpression<ItemStack> {
	static {
		Registration.newExpression(
			SExprItemOfBannerBlock.class,
			ItemStack.class,
			ExpressionType.COMBINED,
			"banner item of [banner] %block%",
			"[banner] %block%['s] banner item"
		);
	}

	private Expression<Block> block;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		block = (Expression<Block>) expr[0];
		return true;
	}

	@Nullable
	@Override
	protected ItemStack[] get(Event e) {
		Block b = block.getSingle(e);
		if (b == null) {
			return null;
		}
		if (b.getType() == Material.STANDING_BANNER || b.getType() == Material.WALL_BANNER) {
			Banner banner = (Banner) b.getState();
			ItemStack item = new ItemStack(Material.BANNER);
			BannerMeta meta = (BannerMeta) item.getItemMeta();
			meta.setPatterns(banner.getPatterns());
			meta.setBaseColor(banner.getBaseColor());
			item.setItemMeta(meta);
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
		return "banner item of " + block.toString(event, b);
	}
}
