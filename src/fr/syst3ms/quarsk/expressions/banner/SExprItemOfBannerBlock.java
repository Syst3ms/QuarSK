package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.Quarsk;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprItemOfBannerBlock extends SimpleExpression<ItemStack> {
    private Expression<Block> block;

    static {
        Quarsk.newExpression("The banner representing a banner block", SExprItemOfBannerBlock.class, ItemStack.class, ExpressionType.COMBINED, "[banner] item of [banner] block %block%", "[banner] %block%['s] [banner] item");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        block = (Expression<Block>) expr[0];
        return true;
    }

    @Override
    protected ItemStack[] get(Event e) {
        if (block != null) {
            if (block.getSingle(e) != null) {
                if (block.getSingle(e).getType() == Material.STANDING_BANNER || block.getSingle(e).getType() == Material.WALL_BANNER) {
                    Banner banner = (Banner) block.getSingle(e).getState();
                    ItemStack item = new ItemStack(Material.BANNER);
                    BannerMeta meta = (BannerMeta) item.getItemMeta();
                    meta.setPatterns(banner.getPatterns());
                    meta.setBaseColor(banner.getBaseColor());
                    item.setItemMeta(meta);
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
