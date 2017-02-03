package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;

/**
 * Created by ARTHUR on 21/01/2017.
 */
public class ExprCustomBannerLayer extends SimpleExpression<Pattern> {
    private Expression<PatternType> pattern;
    private Expression<Color> color;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        pattern = (Expression<PatternType>) expr[0];
        color = (Expression<Color>) expr[1];
        return true;
    }

    @Override
    protected Pattern[] get(Event e) {
        if (pattern != null && color != null) {
            if (pattern.getSingle(e) != null && color.getSingle(e) != null) {
                return new Pattern[]{new Pattern(color.getSingle(e).getWoolColor(), pattern.getSingle(e))};
            }
        }
        return null;
    }

    @Override
    public Class<? extends Pattern> getReturnType() {
        return Pattern.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
