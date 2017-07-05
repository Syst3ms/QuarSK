package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Created by ARTHUR on 22/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprItemLayers extends SimpleExpression<Pattern> {
	static {
		Registration.newExpression(
			SExprItemLayers.class,
			Pattern.class,
			ExpressionType.COMBINED,
			"[banner] pattern[s] of %itemstack%",
			"%itemstack%['s] [banner] pattern[s]"
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
	protected Pattern[] get(Event e) {
		ItemStack i = item.getSingle(e);
		if (i == null) {
			return null;
		}
		if (i.getType() == Material.BANNER || i.getType() == Material.SHIELD) {
			return ((BannerMeta) i.getItemMeta()).getPatterns().stream().toArray(Pattern[]::new);
		}
		return null;
	}

	@Override
	public void change(Event e, Object[] delta, @NotNull Changer.ChangeMode mode) {
		ItemStack i = item.getSingle(e);
		if (i == null) {
			return;
		}
		if (i.getType() == Material.BANNER || i.getType() == Material.SHIELD) {
			BannerMeta meta = ((BannerMeta) i.getItemMeta());
			switch (mode) {
				case ADD:
					Arrays.asList(((Pattern[]) delta)).forEach(meta::addPattern);
					break;
				case SET:
					meta.setPatterns(Arrays.asList(((Pattern[]) delta)));
					break;
				case DELETE:
				case RESET:
					for (int j = 1; j <= meta.numberOfPatterns(); j++) {
						meta.removePattern(j);
					}
					break;
			}
			i.setItemMeta(meta);
		}

	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
			return CollectionUtils.array(Pattern.class);
		} else if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.SET) {
			return CollectionUtils.array(Pattern[].class);
		}
		return null;
	}

	@NotNull
	@Override
	public Class<? extends Pattern> getReturnType() {
		return Pattern.class;
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return "patterns of " + item.toString(event, b);
	}
}
