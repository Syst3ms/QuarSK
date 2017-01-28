package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.util.BannerUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

/**
 * Created by PRODSEB on 28/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprBannerItemFromMnc extends SimpleExpression<ItemStack> {
    private Material material;
    private Expression<String> mncCode;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        material = (parseResult.mark == 0) ? Material.BANNER : Material.SHIELD;
        mncCode = (Expression<String>) expr[0];
        return true;
    }

    @Override
    protected ItemStack[] get(Event e) {
        if (mncCode != null) {
            if (mncCode.getSingle(e) != null) {
                if (BannerUtils.getInstance().isMncPattern(mncCode.getSingle(e))) {
                    ItemStack item = new ItemStack(material);
                    item.setItemMeta(BannerUtils.getInstance().parseMncPattern(mncCode.getSingle(e)));
                    return new ItemStack[]{item};
                }
            }
        }
        return null;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
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
