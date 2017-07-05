package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Syst3ms on 15/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprBeaconEffects extends SimpleExpression<PotionEffect> {
	static {
		Registration.newExpression(
			SExprBeaconEffects.class,
			PotionEffect.class,
			ExpressionType.COMBINED,
			"[the] (0¦(first|primary)|1¦second[ary]) [potion] effect of [beacon] %block%",
			"[beacon] %block%['s] (0¦(first|primary)|1¦second[ary]) [potion] effect"
		);
	}

	private Expression<Block> beacon;
	private boolean isPrimary;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
		beacon = (Expression<Block>) expr[0];
		isPrimary = parseResult.mark == 0;
		return true;
	}

	@Nullable
	@Override
	protected PotionEffect[] get(Event e) {
		Block b = beacon.getSingle(e);
		if (b == null) {
			return null;
		}
		if (b.getType() == Material.BEACON) {
			return new PotionEffect[]{isPrimary
				? ((Beacon) b.getState()).getPrimaryEffect()
				: ((Beacon) b.getState()).getSecondaryEffect()};
		}
		return null;
	}

	@Override
	public void change(Event e, Object[] delta, @NotNull Changer.ChangeMode changeMode) {
		Block b = beacon.getSingle(e);
		if (b == null) {
			return;
		}
		if (b.getType() == Material.BEACON) {
			Beacon state = ((Beacon) b.getState());
			switch (changeMode) {
				case SET:
					if (isPrimary) {
						state.setPrimaryEffect((PotionEffectType) delta[0]);
					} else {
						state.setSecondaryEffect((PotionEffectType) delta[0]);
					}
					break;
				case DELETE:
				case RESET:
					if (isPrimary) {
						state.setPrimaryEffect(null);
					} else {
						state.setSecondaryEffect(null);
					}
					break;
			}
			state.update(true, false);
		}
	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		if (mode != Changer.ChangeMode.REMOVE && mode != Changer.ChangeMode.REMOVE_ALL
			&& mode != Changer.ChangeMode.ADD) {
			return CollectionUtils.array(PotionEffectType.class);
		}
		return null;
	}

	@Nullable
	@Override
	public Class<? extends PotionEffect> getReturnType() {
		return PotionEffect.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Nullable
	@Override
	public String toString(Event event, boolean b) {
		return ((isPrimary) ? "primary " : "secondary ") + "effect of " + beacon.toString(event, b);
	}
}
