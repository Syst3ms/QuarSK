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
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * Created by ARTHUR on 23/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprBannerBlockLayers extends SimpleExpression<Pattern> {
	static {
		Registration.newExpression(
			SExprBannerBlockLayers.class,
			Pattern.class,
			ExpressionType.COMBINED,
			"[banner] pattern[s] of %block%",
			"%block%['s] [banner] (layer|pattern)[s]"
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
	protected Pattern[] get(Event e) {
		Block b = block.getSingle(e);
		if (b == null) {
			return null;
		}
		if (b.getType() == Material.STANDING_BANNER || b.getType() == Material.WALL_BANNER) {
			return ((Banner) b.getState()).getPatterns().stream().toArray(Pattern[]::new);
		}
		return null;
	}

	@Override
	public void change(Event e, Object[] delta, @NotNull Changer.ChangeMode mode) {
		Block b = block.getSingle(e);
		if (b == null) {
			return;
		}
		if (b.getType() == Material.BANNER) {
			Banner banner = (Banner) b.getState();
			Pattern[] patterns = (Pattern[]) delta;
			switch (mode) {
				case ADD:
					Arrays.asList(patterns).forEach(banner::addPattern);
					break;
				case SET:
					banner.setPatterns(Arrays.asList(patterns));
					break;
				case DELETE:
				case RESET:
					for (int i = 1; i <= banner.numberOfPatterns(); i++)
						banner.removePattern(i);
					break;
			}
			banner.update(true, false);
		}
	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
			return CollectionUtils.array(Pattern.class);
		} else if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.SET) {
			return CollectionUtils.array(Pattern[].class);
		} else {
			return null;
		}
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
		return "patterns of " + block.toString(event, b);
	}
}
