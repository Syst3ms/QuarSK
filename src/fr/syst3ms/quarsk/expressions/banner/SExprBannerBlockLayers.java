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

import java.util.Arrays;

/**
 * Created by ARTHUR on 23/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprBannerBlockLayers extends SimpleExpression<Pattern> {
    private Expression<Block> block;

    static {
        Registration.newExpression("The layers of a banner block", SExprBannerBlockLayers.class, Pattern.class, ExpressionType.COMBINED, "[(all|each|every)] [banner] (layer|pattern)[s] of [(block|banner)] %block%", "[(all|every|each) of] %block%['s] [banner] (layer|pattern)[s]");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected Pattern[] get(Event e) {
        if (block != null) {
            if (block.getSingle(e) != null) {
                if (block.getSingle(e).getType() == Material.STANDING_BANNER || block.getSingle(e).getType() == Material.WALL_BANNER) {
                    return ((Banner) block.getSingle(e).getState()).getPatterns().stream().toArray(Pattern[]::new);
                }
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (block != null) {
            if (block.getSingle(e) != null) {
                if (block.getSingle(e).getType() == Material.BANNER) {
                    Banner banner = (Banner) block.getSingle(e).getState();
                    switch (mode) {
                        case ADD:
                            Arrays.asList(((Pattern[]) delta)).forEach(banner::addPattern);
                            break;
                        case SET:
                            banner.setPatterns(Arrays.asList(((Pattern[]) delta)));
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
        }
    }

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


    @Override
    public Class<? extends Pattern> getReturnType() {
        return Pattern.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
