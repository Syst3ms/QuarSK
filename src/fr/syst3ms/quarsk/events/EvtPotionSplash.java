package fr.syst3ms.quarsk.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import fr.syst3ms.quarsk.classes.EnumType;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.util.StringUtils;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Created by ARTHUR on 21/02/2017.
 */
@SuppressWarnings({"unchecked"})
public class EvtPotionSplash extends SkriptEvent {
	static {
		Registration.newEvent(
			"potion splashing",
			EvtPotionSplash.class,
			PotionSplashEvent.class,
			"[potion] splash[ing] [(of|with) %-*potioneffecttypes%]"
		);
	}

	private Literal<PotionEffectType> effectTypesLiteral;

	@Override
	public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
		effectTypesLiteral = (Literal<PotionEffectType>) literals[0];
		return true;
	}

	@Override
	public boolean check(Event e) {
		if (e instanceof PotionSplashEvent) {
			if (effectTypesLiteral.getAll().length > 0) {
				if (effectTypesLiteral.getAll() != null) {
					return ((PotionSplashEvent) e).getEntity()
												  .getEffects()
												  .containsAll(Arrays.asList(effectTypesLiteral.getAll()));
				}
			}
			return true;
		}
		return false;
	}


	@NotNull
	@Override
	public String toString(Event event, boolean b) {
		return "potion splash" + (effectTypesLiteral != null ? " " + StringUtils.englishJoin(Stream.of(
			effectTypesLiteral.getAll(event)).map(t -> EnumType.toString(t.getName())).toArray(String[]::new)) : "");
	}
}
