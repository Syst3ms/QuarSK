package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Created by ARTHUR on 03/02/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprRandomBanner extends SimpleExpression<ItemStack> {
	static {
		Registration.newExpression(ExprRandomBanner.class,
			ItemStack.class,
			ExpressionType.SIMPLE,
			"[a] [new] random (0¦banner|1¦shield)"
		);
	}

	private Material type;

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
		type = parseResult.mark == 0 ? Material.BANNER : Material.SHIELD;
		return true;
	}

	@NotNull
	@Override
	protected ItemStack[] get(Event event) {
		ItemStack banner = new ItemStack(type);
		BannerMeta meta = (BannerMeta) banner.getItemMeta();
		meta.setBaseColor(CollectionUtils.getRandom(DyeColor.values()));
		for (int i = 0; i < new Random().nextInt(7); i++)
			meta.addPattern(new Pattern(CollectionUtils.getRandom(DyeColor.values()),
				CollectionUtils.getRandom(PatternType.values())
			));
		banner.setItemMeta(meta);
		return new ItemStack[]{banner};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@NotNull
	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}

	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return "a random " + type.name().toLowerCase();
	}
}
