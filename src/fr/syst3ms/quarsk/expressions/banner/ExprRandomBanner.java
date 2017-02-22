package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.Quarsk;
import fr.syst3ms.quarsk.util.ListUtils;
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

    static {
        Quarsk.newExpression("A fully randomized banner or shield", ExprRandomBanner.class, ItemStack.class, ExpressionType.SIMPLE, "[a] [new] random (0¦banner|1¦shield)");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        type = parseResult.mark == 0 ? Material.BANNER : Material.SHIELD;
        return true;
    }

    @Override
    protected ItemStack[] get(Event event) {
        ItemStack banner = new ItemStack(type);
        BannerMeta meta = (BannerMeta) banner.getItemMeta();
        meta.setBaseColor(ListUtils.randomElement(DyeColor.values()));
        for (int i = 0; i < new Random().nextInt(7); i++)
            meta.addPattern(new Pattern(ListUtils.randomElement(DyeColor.values()), ListUtils.randomElement(PatternType.values())));
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
