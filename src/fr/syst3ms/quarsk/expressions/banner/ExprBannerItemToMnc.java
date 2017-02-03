package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.util.BannerUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

/**
 * Created by ARTHUR on 03/02/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprBannerItemToMnc extends SimpleExpression<String> {
    private Expression<ItemStack> item;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        item = (Expression<ItemStack>) expr[0];
        return true;
    }

    @Override
    protected String[] get(Event e) {
        if (item != null) {
            if (item.getSingle(e) != null) {
                if (item.getSingle(e).getType() == Material.BANNER || item.getSingle(e).getType() == Material.SHIELD) {
                    return new String[]{BannerUtils.getInstance().toMncPattern((BannerMeta) item.getSingle(e).getItemMeta())};
                }
            }
        }
        return null;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
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
