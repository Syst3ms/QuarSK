package fr.syst3ms.quarsk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.classes.data.SkriptClasses;
import ch.njol.skript.lang.ExpressionType;
import fr.syst3ms.quarsk.effects.EffLinkReference;
import fr.syst3ms.quarsk.effects.EffOrientTowards;
import fr.syst3ms.quarsk.expressions.SExprReference;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.logging.Level;

import static java.lang.Math.PI;

/**
 * Created by Syst3ms on 29/12/2016.
 */
public class QuarSk extends JavaPlugin {

    public static final double RAD_TO_DEG = 180 / PI;

    public void onEnable() {
        getLogger().log(Level.INFO, "There are " + Skript.getEvents().size() + " events, " + Skript.getConditions().size() + " conditions and " + Skript.getEffects().size() + " effects registered !");
        getLogger().log(Level.INFO, "Enabling QuarSk " + this.getConfig().get("version") + " to give these numbers a boost !");
        Skript.registerAddon(this);
        Skript.registerEffect(EffOrientTowards.class, "(orient %entity%|(make %entity%|force %entity% to) (face|look)) (0¦towards|1¦away from) %location%");
        Skript.registerEffect(EffLinkReference.class, "link @<\\S+> to %object%");
        Skript.registerEffect(EffUnlinkReference.class, "unlink @<\\S+>");
        Skript.registerExpression(SExprReference.class,Object.class, ExpressionType.SIMPLE, "@<\\S+>");
        getLogger().log(Level.INFO, "Now, there are " + Skript.getEvents().size() + " events, " + Skript.getConditions().size() + " conditions and " + Skript.getEffects().size() + " effects registered ! Good game !");
    }

    public void onDisable() {
        getLogger().info("Disabling QuarSk 1.0 ! Goodbye !");
    }

    //All of the below functions are by bi0qaw cause I honestly can't do that kind of math. Thanks to you.
    public static Vector vectorFromLocations(Location from, Location to) {
        return new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
    }

    public static float getYaw(Vector vector) {
        if (((Double) vector.getX()).equals((double) 0) && ((Double) vector.getZ()).equals((double) 0)) {
            return 0;
        }
        return (float) (Math.atan2(vector.getZ(), vector.getX()) * RAD_TO_DEG);
    }

    public static float getPitch(Vector vector) {
        double xy = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
        return (float) (Math.atan(vector.getY() / xy) * RAD_TO_DEG);
    }

    public static float notchYaw(float yaw) {
        float y = yaw - 90;
        if (y < -180) {
            y += 360;
        }
        return y;
    }

    public static float notchPitch(float pitch) {
        return -pitch;
    }

    public static void debug(Object... l) {
        for (Object o : l) {
            Bukkit.getLogger().log(Level.INFO, "[QuarSk] " + o.toString());
        }
    }
}


