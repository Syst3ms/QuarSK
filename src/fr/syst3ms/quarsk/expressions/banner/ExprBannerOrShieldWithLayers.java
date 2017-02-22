package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.Quarsk;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Arrays;

/**
 * Created by ARTHUR on 22/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprBannerOrShieldWithLayers extends SimpleExpression<ItemStack> {
    private Material mat;
    private Expression<Pattern> patterns;

    static {
        Quarsk.newExpression("A banner or a shield made of multiple layers and of a base color", ExprBannerOrShieldWithLayers.class, ItemStack.class, ExpressionType.COMBINED, "[new] (0¦banner|1¦shield) (from|with|using|of) [[banner] (layer|pattern)[s]] %bannerlayers%");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        mat = parseResult.mark == 0 ? Material.BANNER : Material.SHIELD;
        patterns = (Expression<Pattern>) expr[0];
        return true;
    }

    @Override
    protected ItemStack[] get(Event e) {
        if (patterns != null) {
            if (patterns.getAll(e).length > 0) {
                ItemStack returnItem = new ItemStack(mat);
                BannerMeta meta = ((BannerMeta) returnItem.getItemMeta());
                meta.setPatterns(Arrays.asList(patterns.getAll(e)));
                returnItem.setItemMeta(meta);
                return new ItemStack[]{returnItem};
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
