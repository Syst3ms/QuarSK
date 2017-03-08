package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.event.Event;

/**
 * Created by ARTHUR on 24/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprBannerBlockBaseColor extends SimpleExpression<Color> {
    private Expression<Block> block;

    static {
        Registration.newExpression("The base color of a banner block", SExprBannerBlockBaseColor.class, Color.class, ExpressionType.COMBINED, "[banner] block bas(e|ic) color of block %block%", "block %block%['s] [banner] bas(e|ic) color");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected Color[] get(Event e) {
        if (block != null) {
            if (block.getSingle(e) != null) {
                if (block.getSingle(e).getType() == Material.STANDING_BANNER || block.getSingle(e).getType() == Material.WALL_BANNER) {
                    Banner banner = ((Banner) block.getSingle(e).getState());
                    return new Color[]{Color.byWoolColor(banner.getBaseColor())};
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
                    Banner banner = ((Banner) block.getSingle(e).getState());
                    switch (mode) {
                        case SET:
                            banner.setBaseColor(((Color) delta[0]).getWoolColor());
                            break;
                        case RESET:
                            banner.setBaseColor(DyeColor.WHITE);
                            break;
                    }
                    banner.update(true, false);
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET) ? CollectionUtils.array(Color.class) : null;
    }

    @Override
    public Class<? extends Color> getReturnType() {
        return Color.class;
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
