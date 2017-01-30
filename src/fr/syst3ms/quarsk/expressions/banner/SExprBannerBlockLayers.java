package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.avaje.ebean.validation.NotNull;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.banner.Pattern;
import org.bukkit.event.Event;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ARTHUR on 23/01/2017.
 */
@SuppressWarnings("unused")
public class SExprBannerBlockLayers extends SimpleExpression<Pattern> {
    private Expression<Block> block;

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
                    Banner banner = ((Banner) block.getSingle(e).getState());
                    return banner.getPatterns().toArray(new Pattern[banner.getPatterns().size()]);
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
                            for (Object pat : delta) {
                                banner.addPattern((Pattern) pat);
                            }
                            break;
                        case SET:
                            List<Pattern> patternList = Arrays.asList((Pattern[]) delta);
                            banner.setPatterns(patternList);
                            break;
                        case DELETE:
                        case RESET:
                            for (int i = 1; i <= banner.numberOfPatterns(); i++) {
                                banner.removePattern(i);
                            }
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
