package fr.syst3ms.quarsk.effects.banner;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.QuarSk;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

/**
 * Created by PRODSEB on 28/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class EffApplyBannerItemToBlock extends Effect {
    private Expression<ItemStack> item;
    private Expression<Block> block;

    static {
        QuarSk.newEffect(EffApplyBannerItemToBlock.class, "apply (banner|shield) [item] pattern[s] of %itemstack% to [banner] [block] %block%", "apply [item] %itemstack%['s] (banner|shield) pattern[s] to [banner] [block] %block%");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        item = (Expression<ItemStack>) expr[0];
        block = (Expression<Block>) expr[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        if (item != null && block != null) {
            if (item.getSingle(e) != null && block.getSingle(e) != null) {
                if ((item.getSingle(e).getType() == Material.BANNER || item.getSingle(e).getType() == Material.SHIELD) && block.getSingle(e).getType() == Material.BANNER) {
                    BannerMeta itemMeta = ((BannerMeta) item.getSingle(e).getItemMeta());
                    Banner blockMeta = ((Banner) block.getSingle(e).getState());
                    blockMeta.setPatterns(itemMeta.getPatterns());
                    blockMeta.setBaseColor(itemMeta.getBaseColor());
                    blockMeta.update(true, false);
                }
            }
        }
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
