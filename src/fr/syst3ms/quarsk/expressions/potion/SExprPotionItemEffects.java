package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.util.PotionUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ARTHUR on 07/01/2017.
 */
public class SExprPotionItemEffects extends SimpleExpression<PotionEffect> {
    private Expression<ItemStack> potionItem;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        potionItem = (Expression<ItemStack>) expr[0];
        return true;
    }

    @Override
    protected PotionEffect[] get(Event e) {
        if (potionItem != null) {
            if (potionItem.getSingle(e) != null) {
                if (PotionUtils.getInstance().isPotionItem(potionItem.getSingle(e))) {
                    PotionMeta meta = (PotionMeta) potionItem.getSingle(e).getItemMeta();
                    List<PotionEffect> list = new ArrayList<>();
                    if (meta.getBasePotionData().getType() != PotionType.UNCRAFTABLE) {
                        list.add(PotionUtils.getInstance().fromPotionData(meta.getBasePotionData()));
                    }
                    for (PotionEffect eff : meta.getCustomEffects()) {
                        list.add(eff);
                    }
                    return list.toArray(new PotionEffect[meta.getCustomEffects().size() + ((meta.getBasePotionData().getType() != PotionType.UNCRAFTABLE) ? 1 : 0)]);
                }
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (potionItem != null) {
            if (potionItem.getSingle(e) != null) {
                if (potionItem.getSingle(e).getType() != Material.AIR) {
                    if (potionItem.getSingle(e).getItemMeta() instanceof PotionMeta) {
                        PotionMeta meta = ((PotionMeta) potionItem.getSingle(e).getItemMeta());
                        switch (mode) {
                            case ADD:
                                for (Object o : delta) {
                                    meta.addCustomEffect(((PotionEffect) o), false);
                                }
                                potionItem.getSingle(e).setItemMeta(meta);
                                break;
                            case SET:
                                meta.clearCustomEffects();
                                meta.setBasePotionData(PotionUtils.getInstance().emptyPotionData());
                                for (Object o : delta) {
                                    meta.addCustomEffect(((PotionEffect) o), false);
                                }
                                potionItem.getSingle(e).setItemMeta(meta);
                                break;
                            case REMOVE:
                            case REMOVE_ALL:
                                for (Object o : delta) {
                                    meta.removeCustomEffect(((PotionEffect) o).getType());
                                    if (PotionUtils.getInstance().fromPotionData(meta.getBasePotionData()).getType() == ((PotionEffect) o).getType()) {
                                        meta.setBasePotionData(PotionUtils.getInstance().emptyPotionData());
                                    }
                                }
                                potionItem.getSingle(e).setItemMeta(meta);
                                break;
                            case DELETE:
                            case RESET:
                                meta.clearCustomEffects();
                                meta.setBasePotionData(PotionUtils.getInstance().emptyPotionData());
                                potionItem.getSingle(e).setItemMeta(meta);
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode != Changer.ChangeMode.REMOVE && mode != Changer.ChangeMode.REMOVE_ALL) {
            return CollectionUtils.array(PotionEffect[].class);
        } else {
            return CollectionUtils.array(PotionEffectType[].class);
        }
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends PotionEffect> getReturnType() {
        return PotionEffect.class;
    }

    @Override
    public String toString(Event e, boolean b) {
        return getClass().getName();
    }
}
