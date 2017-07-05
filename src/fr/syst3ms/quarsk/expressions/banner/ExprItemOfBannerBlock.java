package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
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
public class ExprItemOfBannerBlock extends SimplePropertyExpression<Block, ItemStack> {
	static {
		Registration.newPropertyExpression(
			ExprItemOfBannerBlock.class,
			ItemStack.class,
			"banner item",
			"block"
		);
	}

	@Override
	protected String getPropertyName() {
		return "banner item";
	}

	@Override
	public ItemStack convert(Block block) {
		if (block.getType() == Material.STANDING_BANNER || block.getType() == Material.WALL_BANNER) {
			Banner banner = (Banner) block.getState();
			ItemStack item = new ItemStack(Material.BANNER);
			BannerMeta meta = (BannerMeta) item.getItemMeta();
			meta.setPatterns(banner.getPatterns());
			meta.setBaseColor(banner.getBaseColor());
			item.setItemMeta(meta);
			return item;
		}
		return null;
	}


	@NotNull
	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}
}
