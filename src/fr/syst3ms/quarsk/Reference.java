/**
 * Created by ARTHUR on 29/12/2016.
 */
package fr.syst3ms.quarsk;

import ch.njol.skript.lang.Expression;
import org.bukkit.event.Event;

import java.util.Hashtable;

public class Reference {
    public static Hashtable<String, Reference> refs = new Hashtable<String, Reference>();

    public final String name;
    public Expression<?> expr;
    public Event event;

    public Reference(String name, Expression<?> expr, Event e) {
        this.name = name;
        this.expr = expr;
        this.event = e;
        Reference.refs.put(name, this);
    }

    public static void newReference(String name, Expression<?> expr, Event e) {
        Reference ref = new Reference(name, expr, e);
        Reference.refs.put(name, ref);
    }

    public String toString() {
        return "\nName : " + this.name + "\nExpression : " + this.expr.toString(this.event, false) + "\nEvent : " + this.event.toString();
    }

    public Object getValue() {
        return this.expr.getSingle(this.event);
    }

    public static void clear(String name) {
        Reference.refs.remove(name);
    }

    public static Reference referenceByName(String name) {
        return (Reference.refs.containsKey(name)) ? Reference.refs.get(name) : null;
    }

    public static boolean referenceExists(String name) {
        return Reference.refs.containsKey(name);
    }
}