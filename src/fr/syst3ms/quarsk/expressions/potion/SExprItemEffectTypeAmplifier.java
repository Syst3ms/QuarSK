package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.Quarsk;
import fr.syst3ms.quarsk.util.PotionUtils;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

/**
 * Created by PRODSEB on 27/01/2017.
 */
@SuppressWarnings({"unused","unchecked"})
public class SExprItemEffectTypeAmplifier extends SimpleExpression<Number> {
    private Expression<PotionEffectType> effectType;
    private Expression<ItemStack> item;

    static {
        Quarsk.newExpression("Amplifier of an effect on an item", SExprItemEffectTypeAmplifier.class, Number.class, ExpressionType.COMBINED, "(tier|amplifier) of [[potion] effect [type]] %potioneffecttype% on [item] %itemstack%", "[[potion] effect [type]] %potioneffecttype%['s] (tier|amplifier) on [item] %itemstack%");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        effectType = (Expression<PotionEffectType>) expr[0];
        item = (Expression<ItemStack>) expr[1];
        return true;
    }

    @Override
    protected Number[] get(Event e) {
        if (effectType != null && item != null) {
            if (effectType.getSingle(e) != null && item.getSingle(e) != null) {
                if (PotionUtils.isPotionItem(item.getSingle(e))) {
                    PotionMeta meta = (PotionMeta) item.getSingle(e).getItemMeta();
                    return new Number[]{PotionUtils.getEffectByEffectType(meta, effectType.getSingle(e)).getAmplifier()};
                }
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (effectType != null && item != null) {
            if (effectType.getSingle(e) != null && item.getSingle(e) != null) {
                if (PotionUtils.isPotionItem(item.getSingle(e))) {
                    PotionMeta meta = (PotionMeta) item.getSingle(e).getItemMeta();
                    PotionEffect potionEffect = (meta.getBasePotionData().getType() != PotionType.UNCRAFTABLE) ? PotionUtils.getEffectByEffectType(meta, effectType.getSingle(e)) : PotionUtils.fromPotionData(meta.getBasePotionData());
                    if (meta.getBasePotionData().getType() != PotionType.UNCRAFTABLE) {
                        meta.removeCustomEffect(effectType.getSingle(e));
                    } else {
                        meta.setBasePotionData(PotionUtils.emptyPotionData());
                    }
                    Number number = (Number) delta[0];
                    switch (mode) {
                        case ADD:
                            meta.addCustomEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration(), potionEffect.getAmplifier() + number.intValue(), potionEffect.isAmbient(), potionEffect.hasParticles(), potionEffect.getColor()), true);
                            break;
                        case SET:
                            meta.addCustomEffect(new PotionEffect(potionEffect.getType(),potionEffect.getDuration(), number.intValue(), potionEffect.isAmbient(), potionEffect.hasParticles(), potionEffect.getColor()), true);
                            break;
                        case REMOVE:
                            meta.addCustomEffect(new PotionEffect(potionEffect.getType(), potionEffect.getDuration(), (potionEffect.getAmplifier() - number.intValue() > 0) ? potionEffect.getAmplifier() - number.intValue() : potionEffect.getAmplifier(), potionEffect.isAmbient(), potionEffect.hasParticles(), potionEffect.getColor()), true);
                            break;
                    }
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode != Changer.ChangeMode.REMOVE_ALL && mode != Changer.ChangeMode.RESET && mode != Changer.ChangeMode.DELETE) {
            return CollectionUtils.array(Number.class);
        }
        return null;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
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
