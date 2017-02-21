package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.QuarSk;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by PRODSEB on 31/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprBeaconFuel extends SimpleExpression<ItemStack> {
    private Expression<Block> block;

    static {
        QuarSk.newExpression(SExprBeaconFuel.class, ItemStack.class, ExpressionType.COMBINED, "[the] beacon fuel[ing item[[ ]stack]] of [beacon] %block%", " %block%['s] beacon fuel[ing item[[ ]stack]]");
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
                if (block.getSingle(e).getState() instanceof BeaconInventory) {
                    return new ItemStack[]{((BeaconInventory) block.getSingle(e).getState()).getItem()};
                }
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (block != null) {
            if (block.getSingle(e) != null) {
                if (block.getSingle(e).getState() instanceof BeaconInventory) {
                    ((BeaconInventory) block.getSingle(e).getState()).setItem((ItemStack) delta[0]);
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return (mode == Changer.ChangeMode.SET) ? CollectionUtils.array(ItemStack.class) : null;
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
