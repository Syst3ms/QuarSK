package fr.syst3ms.quarsk.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.material.Banner;

/**
 * Created by PRODSEB on 30/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class CondIsWallBanner extends Condition {
    private Expression<Block> block;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) expr[0];
        setNegated(parseResult.mark == 1);
        return true;
    }

    @Override
    public boolean check(Event e) {
        if (block != null) {
            if (block.getSingle(e) != null) {
                if (block.getSingle(e).getState() instanceof Banner) {
                    Banner banner = (Banner) block.getSingle(e).getState();
                    return isNegated() != banner.isWallBanner();
                }
            }
        }
        return false;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
