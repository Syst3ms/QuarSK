package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ARTHUR on 21/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprCustomBannerLayer extends SimpleExpression<Pattern> {
	static {
		Registration.newExpression(
			ExprCustomBannerLayer.class,
			Pattern.class,
			ExpressionType.COMBINED,
			"[new] [banner] (layer|pattern) (with|using) (pattern [type]|type) %bannerpattern% [(with|and)] colo[u]r %color%"
		);
	}

	private Expression<PatternType> pattern;
	private Expression<Color> color;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		pattern = (Expression<PatternType>) expr[0];
		color = (Expression<Color>) expr[1];
		return true;
	}

	@Nullable
	@Override
	protected Pattern[] get(Event e) {
		Color c = color.getSingle(e);
		PatternType p = pattern.getSingle(e);
		if (c == null || p == null) {
			return null;
		}
		return new Pattern[]{new Pattern(c.getWoolColor(), p)};
	}

	@NotNull
	@Override
	public Class<? extends Pattern> getReturnType() {
		return Pattern.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return "banner pattern with type " + pattern.toString(event, b) + " and color " + color.toString(event, b);
	}
}
