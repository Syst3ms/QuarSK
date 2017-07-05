package fr.syst3ms.quarsk.expressions.beacon;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by PRODSEB on 31/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprBeaconFuel extends SimplePropertyExpression<Block, ItemStack> {
	static {
		Registration.newPropertyExpression(SExprBeaconFuel.class, ItemStack.class, "beacon fuel[ing] [item]", "block");
	}

	@Nullable
	@Override
	public ItemStack convert(@NotNull Block block) {
		BlockState state = block.getState();
		return state instanceof BeaconInventory ? ((BeaconInventory) state).getItem() : null;
	}

	@NotNull
	@Override
	protected String getPropertyName() {
		return "beacon fuel";
	}

	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
		Block b = getExpr().getSingle(e);
		if (b == null) {
			return;
		}
		if (b.getState() instanceof BeaconInventory) {
			((BeaconInventory) b.getState()).setItem((ItemStack) delta[0]);
		}
	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		return mode == Changer.ChangeMode.SET ? CollectionUtils.array(ItemStack.class) : null;
	}

	@NotNull
	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}
}
