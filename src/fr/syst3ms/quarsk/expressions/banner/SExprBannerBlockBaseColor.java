package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.util.Color;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ARTHUR on 24/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprBannerBlockBaseColor extends SimplePropertyExpression<Block, Color> {
	static {
		Registration.newPropertyExpression(SExprBannerBlockBaseColor.class, Color.class, "bas(e|ic) color", "block");
	}

	private Expression<Block> block;

	@Override
	@Nullable
	public Color convert(@NotNull Block block) {
		BlockState state = block.getState();
		return state instanceof Banner ? Color.byWoolColor(((Banner) state).getBaseColor()) : null;
	}

	@NotNull
	@Override
	protected String getPropertyName() {
		return "base color";
	}

	@Override
	public void change(Event e, Object[] delta, @NotNull Changer.ChangeMode mode) {
		Block b = block.getSingle(e);
		if (b == null) {
			return;
		}
		if (b.getType() == Material.BANNER) {
			Banner banner = ((Banner) b.getState());
			switch (mode) {
				case SET:
					Color c = (Color) delta[0];
					banner.setBaseColor(c.getWoolColor());
					break;
				case RESET:
					banner.setBaseColor(DyeColor.WHITE);
					break;
			}
			banner.update(true, false);
		}

	}

	@Nullable
	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		return (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.RESET)
			? CollectionUtils.array(Color.class)
			: null;
	}

	@NotNull
	@Override
	public Class<? extends Color> getReturnType() {
		return Color.class;
	}
}
