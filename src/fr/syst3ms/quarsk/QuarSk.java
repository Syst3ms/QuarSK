package fr.syst3ms.quarsk;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import com.google.common.collect.Iterators;
import com.sun.istack.internal.Nullable;
import fr.syst3ms.quarsk.conditions.CondHasPotionEffect;
import fr.syst3ms.quarsk.effects.EffLinkReference;
import fr.syst3ms.quarsk.effects.EffOrientTowards;
import fr.syst3ms.quarsk.effects.EffUnlinkReference;
import fr.syst3ms.quarsk.effects.potion.EffApplyPotionEffects;
import fr.syst3ms.quarsk.effects.potion.EffMilkEntity;
import fr.syst3ms.quarsk.expressions.SExprReference;
import fr.syst3ms.quarsk.expressions.beacon.ExprBeaconTier;
import fr.syst3ms.quarsk.expressions.beacon.ExprEntitiesInRange;
import fr.syst3ms.quarsk.expressions.beacon.SExprBeaconEffects;
import fr.syst3ms.quarsk.expressions.potion.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import java.util.logging.Level;

import static ch.njol.skript.Skript.*;


/**
 * Created by Syst3ms on 29/12/2016.
 */
public class QuarSk extends JavaPlugin {

    public static final double RAD_TO_DEG = 180 / Math.PI;
    private static QuarSk instance;

    public void onEnable() {
        instance = this;
        getLogger().log(Level.INFO, "There are " + getEvents().size() + " events, " + getConditions().size() + " conditions, " + getEffects().size() + " effects and " + Iterators.size(getExpressions()) + " expressions registered !");
        getLogger().log(Level.INFO, "Enabling QuarSk " + getVersion() + " to give these numbers a boost !");
        normalRegister();
        getLogger().log(Level.INFO, "Now, there are " + getEvents().size() + " events, " + getConditions().size() + " conditions and " + getEffects().size() + " effects and " + Iterators.size(getExpressions()) + " expressions registered ! Good game !");
    }

    public void onDisable() {
        getLogger().info("Disabling QuarSk 1.0 ! Goodbye !");
    }

    private void normalRegister() {
        registerAddon(this);
        /*
         * TYPES
         */
        //Potions
        Classes.registerClass(new ClassInfo<PotionEffect>(PotionEffect.class, "potioneffect")
                .name("potion effect")
                .description("A getter for potion effects")
                .parser(new Parser<PotionEffect>() {
                            @Override
                            @Nullable
                            public PotionEffect parse(String obj, ParseContext context) {
                                return null;
                            }

                            @Override
                            public String toString(PotionEffect potionEffect, int i) {
                                return "effect" + potionEffect.getType() + ", tier " + potionEffect.getAmplifier() + ", duration " + potionEffect.getDuration() + ", particles " + potionEffect.hasParticles() + ", ambient " + potionEffect.isAmbient() + ", color " + potionEffect.getColor();
                            }

                            @Override
                            public String toVariableNameString(PotionEffect potionEffect) {
                                return potionEffect.getType() + "," + potionEffect.getAmplifier() + "," + potionEffect.getDuration() + "," + potionEffect.hasParticles() + "," + potionEffect.isAmbient() + "," + potionEffect.getColor();
                            }

                            public String getVariableNamePattern() {
                                return ".+";
                            }
                        }
                )
        );
        /*
         * EFFECTS
         */
        //Orientation
        registerEffect(EffOrientTowards.class, "orient %livingentity% (0¦towards|1¦away from) %location%", "make %livingentity% (face|look [at]) (0¦[towards]|1¦away from) %location%", "force %livingentity% to (face|look [at]) (0¦[towards]|1¦away from) %location%");
        //References
        registerEffect(EffLinkReference.class, "link @<\\S+> to %object%");
        registerEffect(EffUnlinkReference.class, "unlink @<\\S+>");
        registerEffect(EffApplyPotionEffects.class, "apply [potion] [effect[s] [of]] %potioneffects% to %livingentities%");
        registerEffect(EffMilkEntity.class, "milk %livingentities%");
        /*
         * CONDITIONS
         */
        //Potions
        registerCondition(CondHasPotionEffect.class, "[entity] %livingentity% (0¦has [got]|1¦has( not|n't) [got]) [(the|a)] %potioneffecttype% [potion] effect");
        /*
         * EXPRESSIONS
         */
        //References
        registerExpression(SExprReference.class, Object.class, ExpressionType.SIMPLE, "@<\\S+>");
        //Potions
        registerExpression(ExprCustomPotionEffect.class, PotionEffect.class, ExpressionType.COMBINED, "[[potion] effect [(with|by)]] %potioneffecttype% for %timespan% with [a] [tier [of]] %number% [particles %-boolean%[ with ambient [effect] %-boolean%[ and [particle] colo[u]r[ed] %-color%]]]]]");
        registerExpression(ExprCustomPotionItem.class, ItemStack.class, ExpressionType.COMBINED, "(0¦[normal] potion|1¦splash potion|2¦linger[ing] potion|3¦(potion|tipped) arrow) (of|by|with|from|using) [effect[s]] %potioneffects%");
        registerExpression(ExprEntityPotionEffects.class, PotionEffect.class, ExpressionType.COMBINED, "[(all|every|each)] [active] [potion] effects (on|in) %livingentities%", "[(every|all|each) of] %livingentities%['s] [active] [potion] effect[s]");
        registerExpression(SExprPotionItemEffects.class, PotionEffect.class, ExpressionType.COMBINED, "[(all|every|each)] [potion] effect[s] (on|of) %itemstack%", "[(all|every|each) of] %itemstack%['s] [potion] effect[s]");
        registerExpression(ExprPotionEffectType.class, PotionEffectType.class, ExpressionType.COMBINED, "potion[ ]effect[[ ]type][s] of %potioneffect%", "%potioneffect%['s] potion[ ]effect[[ ]type][s]");
        registerExpression(ExprPotionEffectDuration.class, Timespan.class, ExpressionType.COMBINED, "(duration|length) of [potion] effect[s] %potioneffect%", "[potion] effect[s] %potioneffect%['s] (duration|length)");
        registerExpression(ExprPotionEffectTier.class, Number.class, ExpressionType.COMBINED, "(tier|level|amplifier|power) of [potion] [effect] %potioneffect%", "[potion] [effect] %potioneffect%['s] (tier|amplifier|level|power)");
        //Beacons
        registerExpression(ExprEntitiesInRange.class, LivingEntity.class, ExpressionType.COMBINED, "[(all|every|each)] ([living] entit(ies|y)|player[s]) in range of %block%");
        registerExpression(ExprBeaconTier.class, Number.class, ExpressionType.COMBINED, "(tier|level) of [beacon] %block%", "[beacon] %block%['s] (tier|level)");
        registerExpression(SExprBeaconEffects.class, PotionEffect.class, ExpressionType.COMBINED, "[the] (0¦(first|primary)|1¦second[ary]) [potion] effect of [beacon] %block%", "[beacon] %block%['s] (0¦(first|primary)|1¦second[ary]) [potion] effect");
    }


    //Potions
    //I know I could have used ternary operators, but it makes the code much less readable
    public static PotionEffect fromPotionData(PotionData data) {
        PotionEffectType type = data.getType().getEffectType();
        if (type == PotionEffectType.HEAL || type == PotionEffectType.HARM) { //Instant potions
            if (data.isUpgraded()) {
                return new PotionEffect(type, 1, 2);
            } else {
                return new PotionEffect(type, 1, 1);
            }
        } else if (type == PotionEffectType.REGENERATION || type == PotionEffectType.POISON) { //Regen and poison potions have smaller durations
            if (data.isExtended()) {
                return new PotionEffect(type, 1800, 1);
            } else if (data.isUpgraded()) {
                return new PotionEffect(type, 440, 2);
            } else {
                return new PotionEffect(type, 900, 1);
            }
        } else if (type == PotionEffectType.NIGHT_VISION || type == PotionEffectType.INVISIBILITY || type == PotionEffectType.FIRE_RESISTANCE || type == PotionEffectType.WATER_BREATHING) { //Potions that don't have an upgraded version
            if (data.isExtended()) {
                return new PotionEffect(type, 9600, 1);
            } else {
                return new PotionEffect(type, 3600, 1);
            }
        } else if (type == PotionEffectType.WEAKNESS || type == PotionEffectType.SLOW) { //Those potions don't have an upgraded version AND have smaller durations
            if (data.isExtended()) {
                return new PotionEffect(type, 4800, 1);
            } else {
                return new PotionEffect(type, 1800, 1);
            }
        } else {
            if (data.isExtended()) {
                return new PotionEffect(type, 9600, 1);
            } else if (data.isUpgraded()) {
                return new PotionEffect(type, 1800, 2);
            } else {
                return new PotionEffect(type, 3600, 1);
            }
        }
    }

    public static PotionData emptyPotionData() {
        return new PotionData(PotionType.WATER);
    }

    //Conventions
    public static String getPrefix() {
        return "[QuarSk] ";
    }

    public static QuarSk getInstance() {
        return instance;
    }

    public static String getVersion() {
        return "1.1";
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
            Bukkit.getLogger().log(Level.INFO, getPrefix() + " " + o.toString());
        }
    }
}

