package fr.syst3ms.quarsk.expressions.potion;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.classes.EnumType;
import fr.syst3ms.quarsk.classes.Registration;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Created by ARTHUR on 07/01/2017.
 */
@SuppressWarnings({"unchecked"})
public class ExprCustomPotionItem extends SimpleExpression<ItemStack> {
	static {
		Registration.newExpression(
			ExprCustomPotionItem.class,
			ItemStack.class,
			ExpressionType.COMBINED,
			"(0¦[normal] potion|1¦splash potion|2¦linger[ing] potion|3¦(potion|tipped) arrow) (with|using) [effects] %potioneffects%"
		);
	}

	private Material material;
	private Expression<PotionEffect> potionEffects;

	@Override
	public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, @NotNull SkriptParser.ParseResult parseResult) {
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
			case 3:
				material = Material.TIPPED_ARROW;
				break;
			default:
				material = Material.POTION;
				break;
		}
		potionEffects = (Expression<PotionEffect>) expr[0];
		return true;
	}

	@NotNull
	@Override
	protected ItemStack[] get(Event e) {
		ItemStack item = new ItemStack(material, 1);
		PotionMeta meta = (PotionMeta) item.getItemMeta(); //Getting PotionMeta
		Stream.of(potionEffects.getAll(e)).forEach(eff -> meta.addCustomEffect(eff, true));
		item.setItemMeta(meta);
		return new ItemStack[]{item};
	}

	@NotNull
	@Override
	public Class getReturnType() {
		return ItemStack.class;
	}

	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return EnumType.toString(material.name()) + " using effects " + potionEffects.toString(event, b);
	}

	@Override
	public boolean isSingle() {
		return true;
	}
}
