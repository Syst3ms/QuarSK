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
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

/**
 * Created by ARTHUR on 23/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprItemBaseColor extends SimpleExpression<Color> {
    private Expression<ItemStack> item;

    static {
        Registration.newExpression("The base color of a shield or a banner", SExprItemBaseColor.class, Color.class, ExpressionType.COMBINED, "[(banner|shield)] bas(e|ic) color of item %itemstack%", "item %itemstack%['s] [(banner|shield)] bas(e|ic) color");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        item = (Expression<ItemStack>) expr[0];
        return true;
    }

    @Override
    protected Color[] get(Event e) {
        if (item != null) {
            if (item.getSingle(e) != null) {
                if (item.getSingle(e).getType() == Material.BANNER || item.getSingle(e).getType() == Material.SHIELD) {
                    return new Color[]{Color.byWoolColor(((BannerMeta) item.getSingle(e).getItemMeta()).getBaseColor())};
                }
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (item != null) {
            if (item.getSingle(e) != null) {
                if (item.getSingle(e).getType() == Material.BANNER || item.getSingle(e).getType() == Material.SHIELD) {
                    BannerMeta meta = ((BannerMeta) item.getSingle(e).getItemMeta());
                    switch (mode) {
                        case SET:
                            meta.setBaseColor(((Color) delta[0]).getWoolColor());
                            break;
                        case RESET:
                            meta.setBaseColor(DyeColor.WHITE);
                            break;
                    }
                    item.getSingle(e).setItemMeta(meta);
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
