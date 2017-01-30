package fr.syst3ms.quarsk.conditions;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.material.Banner;

import java.util.Comparator;

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
                if (block.getSingle(e).getType() == Material.STANDING_BANNER || block.getSingle(e).getType() == Material.WALL_BANNER) {
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
