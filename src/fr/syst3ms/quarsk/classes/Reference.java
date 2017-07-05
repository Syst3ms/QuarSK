/**
 * Created by ARTHUR on 29/12/2016.
 */
package fr.syst3ms.quarsk.classes;

import ch.njol.skript.lang.Expression;
import org.bukkit.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Reference {
	private final static Map<String, Reference> referenceMap = new HashMap<>();

	private final String name;
	private final Expression<?> expr;
	private final Event event;

	private Reference(String name, Expression<?> expr, Event event) {
		this.name = name;
		this.expr = expr;
		this.event = event;
	}

	public static Optional<Reference> byName(String name) {
		return Optional.ofNullable(referenceMap.getOrDefault(name, null));
	}

	public static void newInstance(String name, Expression<?> value, Event e) {
		referenceMap.put(name, new Reference(name, value, e));
	}

	public static void removeReference(String name) {
		referenceMap.remove(name);
	}

	public String getName() {
		return name;
	}

	public Expression<?> getExpr() {
		return expr;
	}

	public Event getEvent() {
		return event;
	}
}