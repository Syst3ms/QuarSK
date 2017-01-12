package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

/**
 * Created by ARTHUR on 07/01/2017.
 */
public class ExprCustomPotionItem extends SimpleExpression {
    private Material material;
    private Expression<PotionEffect> potionEffects;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        switch (parseResult.mark) {
            case 0:
                material = Material.POTION;
                break;
            case 1:
                material = Material.SPLASH_POTION;
                break;
            case 2:
                material = Material.LINGERING_POTION;
                break;
            default:
                material = Material.POTION;
                break;
        }
        potionEffects = (Expression<PotionEffect>) expr[0];
        return true;
    }

    @Override
    protected Object[] get(Event e) {
        ItemStack item = new ItemStack(material, 1);
        PotionMeta meta = ((PotionMeta)item.getItemMeta()); //Getting PotionMeta
        PotionEffect[] effect = potionEffects.getAll(e);
        for (PotionEffect eff : effect) {
            meta.addCustomEffect(eff, true);
        }
        item.setItemMeta(meta);
        return new ItemStack[]{item};
    }

    @Override
    public Class getReturnType() {
        return ItemStack.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }
}
