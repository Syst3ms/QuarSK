package fr.syst3ms.quarsk.effects.banner;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Created by PRODSEB on 28/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class EffApplyBannerItemToBlock extends Effect {
	static {
		Registration.newEffect(
			EffApplyBannerItemToBlock.class,
			"apply [(banner|shield)] patterns of %itemstack% to %block%",
			"apply %itemstack%['s] [(banner|shield)] pattern[s] to %block%"
		);
	}

	private Expression<ItemStack> item;
	private Expression<Block> block;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		item = (Expression<ItemStack>) expr[0];
		block = (Expression<Block>) expr[1];
		return true;
	}

	@Override
	protected void execute(Event e) {
		ItemStack i = item.getSingle(e);
		Block b = block.getSingle(e);
		if (i == null || b == null) {
			return;
		}
		if ((i.getType() == Material.BANNER || i.getType() == Material.SHIELD) && b.getType() == Material.BANNER) {
			BannerMeta itemMeta = ((BannerMeta) i.getItemMeta());
			Banner blockMeta = ((Banner) b.getState());
			blockMeta.setPatterns(itemMeta.getPatterns());
			blockMeta.setBaseColor(itemMeta.getBaseColor());
			blockMeta.update(true, false);
		}
	}

	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return "apply patterns of " + item.toString(event, b) + " to " + block.toString(event, b);
	}
}
