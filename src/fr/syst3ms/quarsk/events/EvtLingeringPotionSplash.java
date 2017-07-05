package fr.syst3ms.quarsk.events;

import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import fr.syst3ms.quarsk.classes.EnumType;
import fr.syst3ms.quarsk.classes.Registration;
import fr.syst3ms.quarsk.util.StringUtils;
import org.bukkit.event.Event;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class EvtLingeringPotionSplash extends SkriptEvent {
	static {
		Registration.newEvent(
			"Lingering potion splash",
			EvtLingeringPotionSplash.class,
			LingeringPotionSplashEvent.class,
			"linger[ing] potion splash[ing] [with [effect] types %-*potioneffecttypes*]"
		);
	}

	private Literal<PotionEffectType> effectTypes;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Literal<?>[] literals, int i, SkriptParser.ParseResult parseResult) {
		effectTypes = (Literal<PotionEffectType>) literals[0];
		return true;
	}

	@SuppressWarnings("SimplifiableIfStatement")
	@Override
	public boolean check(Event event) {
		if (event instanceof LingeringPotionSplashEvent) {
			if (effectTypes != null && effectTypes.getArray().length > 0) {
				return ((LingeringPotionSplashEvent) event).getEntity()
														   .getEffects()
														   .containsAll(Arrays.asList(effectTypes.getArray()));
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString(Event event, boolean b) {
		return "lingering potion splash" + (effectTypes == null
			? ""
			: " with types " + StringUtils.englishJoin(Arrays.stream(effectTypes.getArray())
															 .map(t -> EnumType.toString(t.getName()))
															 .toArray(String[]::new)));
	}
}
