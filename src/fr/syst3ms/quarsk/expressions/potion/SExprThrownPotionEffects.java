package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.util.PotionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by PRODSEB on 29/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprThrownPotionEffects extends SimpleExpression<PotionEffect> {
    private Expression<Entity> entity;

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        entity = (Expression<Entity>) expr[0];
        return true;
    }

    @Override
    protected PotionEffect[] get(Event e) {
        if (entity != null) {
            if (entity.getSingle(e) != null) {
                if (PotionUtils.getInstance().isEntityThrownPotion(entity.getSingle(e))) {
                    ThrownPotion thrownPotion = ((ThrownPotion) entity.getSingle(e));
                    return thrownPotion.getEffects().toArray(new PotionEffect[thrownPotion.getEffects().size()]);
                } else if (entity.getSingle(e).getType() == EntityType.TIPPED_ARROW) {
                    TippedArrow tippedArrow = ((TippedArrow) entity.getSingle(e));
                    return tippedArrow.getCustomEffects().toArray(new PotionEffect[tippedArrow.getCustomEffects().size()]);
                }
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (entity != null) {
            if (PotionUtils.getInstance().isEntityThrownPotion(entity.getSingle(e))) {
                ItemStack item = ((ThrownPotion) entity.getSingle(e)).getItem();
                PotionMeta potionMeta = ((PotionMeta) ((ThrownPotion) entity.getSingle(e)).getItem().getItemMeta());
                switch (mode) {
                    case ADD:
                        if (delta[0] instanceof PotionEffect) {
                            for (PotionEffect effect : (PotionEffect[]) delta) {
                                potionMeta.addCustomEffect(effect, true);
                            }
                        }
                        break;
                    case SET:
                        if (delta[0] instanceof PotionEffect) {
                            potionMeta.clearCustomEffects();
                            for (PotionEffect effect : (PotionEffect[]) delta) {
                                potionMeta.addCustomEffect(effect, true);
                            }
                        }
                        break;
                    case REMOVE:
                        if (delta[0] instanceof PotionEffectType) {
                            potionMeta.removeCustomEffect((PotionEffectType) delta[0]);
                        }
                        break;
                    case DELETE:
                        potionMeta.clearCustomEffects();
                        break;
                }
                item.setItemMeta(potionMeta);
                ((ThrownPotion) entity.getSingle(e)).setItem(item);
            } else if (entity.getSingle(e).getType() == EntityType.TIPPED_ARROW) {
                TippedArrow tippedArrow = (TippedArrow) entity.getSingle(e);
                switch (mode) {
                    case ADD:
                        if (delta[0] instanceof PotionEffect) {
                            for (PotionEffect effect : (PotionEffect[]) delta) {
                                tippedArrow.addCustomEffect(effect, true);
                            }
                        }
                        break;
                    case SET:
                        if (delta[0] instanceof PotionEffect) {
                            tippedArrow.clearCustomEffects();
                            for (PotionEffect effect : (PotionEffect[]) delta) {
                                tippedArrow.addCustomEffect(effect, true);
                            }
                        }
                        break;
                    case REMOVE:
                        if (delta[0] instanceof PotionEffectType) {
                            tippedArrow.removeCustomEffect((PotionEffectType) delta[0]);
                        }
                        break;
                    case DELETE:
                        tippedArrow.clearCustomEffects();
                        break;
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.REMOVE) {
            return CollectionUtils.array(PotionEffectType.class);
        } else if (mode != Changer.ChangeMode.RESET) {
            return CollectionUtils.array(PotionEffect[].class);
        }
        return null;
    }

    @Override
    public Class<? extends PotionEffect> getReturnType() {
        return PotionEffect.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public String toString(Event event, boolean b) {
        return getClass().getName();
    }
}
