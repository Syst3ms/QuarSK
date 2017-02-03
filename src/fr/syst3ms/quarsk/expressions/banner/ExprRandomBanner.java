package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.Random;

/**
 * Created by ARTHUR on 03/02/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class ExprRandomBanner extends SimpleExpression<ItemStack> {
    private Material type;

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        type = parseResult.mark == 0 ? Material.BANNER : Material.SHIELD;
        return true;
    }

    @Override
    protected ItemStack[] get(Event event) {
        ItemStack banner = new ItemStack(type);
        BannerMeta meta = (BannerMeta) banner.getItemMeta();
        meta.setBaseColor(DyeColor.values()[new Random().nextInt(DyeColor.values().length)]);
        for (int i = 0; i < (new Random().nextInt(5)) + 1; i++) {
            meta.addPattern(new Pattern(
                    DyeColor.values()[new Random().nextInt(DyeColor.values().length)],
                    PatternType.values()[new Random().nextInt(PatternType.values().length)]
            ));
        }
        banner.setItemMeta(meta);
        return new ItemStack[]{banner};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
