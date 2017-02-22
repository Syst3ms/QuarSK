package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.Quarsk;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Syst3ms on 15/01/2017.
 */
@SuppressWarnings({"unused", "unchecked"})
public class SExprBeaconEffects extends SimpleExpression<PotionEffect> {
    private Expression<Block> beacon;
    private int mode;

    static {
        Quarsk.newExpression("Potion effects of a beacon", SExprBeaconEffects.class, PotionEffect.class, ExpressionType.COMBINED, "[the] (0¦(first|primary)|1¦second[ary]) [potion] effect of [beacon] %block%", "[beacon] %block%['s] (0¦(first|primary)|1¦second[ary]) [potion] effect");
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        beacon = (Expression<Block>) expr[0];
        mode = parseResult.mark;
        return true;
    }

    @Override
    protected PotionEffect[] get(Event e) {
        if (beacon.getSingle(e) != null) {
            if (beacon.getSingle(e).getType() == Material.BEACON) {
                return new PotionEffect[]{mode == 0 ? ((Beacon) beacon.getSingle(e).getState()).getPrimaryEffect() : ((Beacon) beacon.getSingle(e).getState()).getSecondaryEffect()};
            }
        }
        return null;
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode changeMode) {
        if (beacon.getSingle(e) != null) {
            if (beacon.getSingle(e).getType() == Material.BEACON) {
                Beacon state = ((Beacon) beacon.getSingle(e).getState());
                switch (changeMode) {
                    case SET:
                        if (mode == 0) {
                            state.setPrimaryEffect((PotionEffectType) delta[0]);
                        } else {
                            state.setSecondaryEffect((PotionEffectType) delta[0]);
                        }
                        break;
                    case DELETE:
                    case RESET:
                        if (mode == 0) {
                            state.setPrimaryEffect(null);
                        } else {
                            state.setSecondaryEffect(null);
                        }
                        break;
                }
                state.update(true, false);
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode != Changer.ChangeMode.REMOVE && mode != Changer.ChangeMode.REMOVE_ALL && mode != Changer.ChangeMode.ADD) {
            return CollectionUtils.array(PotionEffectType.class);
        }
        return null;
    }

    @Override
    public Class<? extends PotionEffect> getReturnType() {
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(Event event, boolean b) {
        return null;
    }
}
