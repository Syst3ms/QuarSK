package fr.syst3ms.quarsk.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.syst3ms.quarsk.QuarSk;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Created by Raphaëlle on 04/01/2017.
 */
public class EffCsGiveWeapon extends Effect {
    private Expression<Number> amount;
    private Expression<String> weaponName;
    private Expression<Player> player;

    @Override
    protected void execute(Event e) {
        QuarSk.csInstance.giveWeapon(player.getSingle(e), weaponName.getSingle(e), amount.getSingle(e).intValue());
    }

    @Override
    public String toString(Event e, boolean b) {
        return e.toString();
    }

    @Override
    public boolean init(Expression<?>[] expr, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        amount = (Expression<Number>) expr[0];
        weaponName = (Expression<String>) expr[1];
        player = (Expression<Player>) expr[2];
        return true;
    }
}
