package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
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
public class SExprItemLayers extends SimpleExpression<Pattern> {
    private Expression<ItemStack> item;

    static {
        Registration.newExpression("The layers of a banner or a shield", SExprItemLayers.class, Pattern.class, ExpressionType.COMBINED, "[(all|each|every)] [banner] (layer|pattern)[s] of [(shield|banner|item)] %itemstack%", "[(all|every|each) of] %itemstack%['s] [banner] (layer|pattern)[s]");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        item = (Expression<ItemStack>) expr[0];
        return true;
    }

    @Override
    protected Pattern[] get(Event e) {
        if (item != null) {
            if (item.getSingle(e) != null) {
                if (item.getSingle(e).getType() == Material.BANNER || item.getSingle(e).getType() == Material.SHIELD) {
                    return ((BannerMeta) item.getSingle(e).getItemMeta()).getPatterns().stream().toArray(Pattern[]::new);
                }
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (item != null) {
            if (item.getSingle(e) != null) {
                if (item.getSingle(e).getType() == Material.BANNER || item.getSingle(e).getType() == Material.SHIELD) {
                    BannerMeta meta = ((BannerMeta) item.getSingle(e).getItemMeta());
                    switch (mode) {
                        case ADD:
                            Arrays.asList(((Pattern[]) delta)).forEach(meta::addPattern);
                            break;
                        case SET:
                            meta.setPatterns(Arrays.asList(((Pattern[]) delta)));
                            break;
                        case DELETE:
                        case RESET:
                            for (int i = 1; i <= meta.numberOfPatterns(); i++) {
                                meta.removePattern(i);
                            }
                            break;
                    }
                    item.getSingle(e).setItemMeta(meta);
                }
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) {
            return CollectionUtils.array(Pattern.class);
        } else if (mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Pattern[].class);
        }
        return null;
    }

    @Override
    public Class<? extends Pattern> getReturnType() {
        return Pattern.class;
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
