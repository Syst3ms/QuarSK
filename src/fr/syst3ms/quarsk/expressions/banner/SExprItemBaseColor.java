package fr.syst3ms.quarsk.expressions.banner;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.util.Color;
import ch.njol.util.coll.CollectionUtils;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ARTHUR on 23/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class SExprItemBaseColor extends SimplePropertyExpression<ItemStack, Color> {
	static {
		Registration.newPropertyExpression(SExprItemBaseColor.class, Color.class, "bas(e|ic) color", "itemstack");
	}

	private Expression<ItemStack> item;

	@Nullable
	@Override
	public Color convert(@NotNull ItemStack itemStack) {
		ItemMeta meta = itemStack.getItemMeta();
		return meta instanceof BannerMeta ? Color.byWoolColor(((BannerMeta) meta).getBaseColor()) : null;
	}

	@NotNull
	@Override
	protected String getPropertyName() {
		return "base color";
	}

	@Override
	public void change(Event e, Object[] delta, @NotNull Changer.ChangeMode mode) {
		ItemStack i = item.getSingle(e);
		if (i == null) {
			return;
		}
		if (i.getType() == Material.BANNER || i.getType() == Material.SHIELD) {
			BannerMeta meta = ((BannerMeta) i.getItemMeta());
			switch (mode) {
				case SET:
					Color c = (Color) delta[0];
					meta.setBaseColor(c.getWoolColor());
					break;
				case RESET:
					meta.setBaseColor(DyeColor.WHITE);
					break;
			}
			i.setItemMeta(meta);
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
