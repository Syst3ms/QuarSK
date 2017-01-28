package fr.syst3ms.quarsk;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.*;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.Timespan;
import com.sun.istack.internal.Nullable;
import fr.syst3ms.quarsk.classes.EnumType;
import fr.syst3ms.quarsk.conditions.CondHasPotionEffect;
import fr.syst3ms.quarsk.effects.EffLinkReference;
import fr.syst3ms.quarsk.effects.EffOrientTowards;
import fr.syst3ms.quarsk.effects.EffUnlinkReference;
import fr.syst3ms.quarsk.effects.potion.EffApplyPotionEffects;
import fr.syst3ms.quarsk.effects.potion.EffMilkEntity;
import fr.syst3ms.quarsk.expressions.SExprReference;
import fr.syst3ms.quarsk.expressions.banner.*;
import fr.syst3ms.quarsk.expressions.beacon.ExprBeaconTier;
import fr.syst3ms.quarsk.expressions.beacon.ExprEntitiesInRange;
import fr.syst3ms.quarsk.expressions.beacon.SExprBeaconEffects;
import fr.syst3ms.quarsk.expressions.potion.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static ch.njol.skript.Skript.registerAddon;


@SuppressWarnings({"unused", "unchecked"})
public class QuarSk extends JavaPlugin {

    private static final double RAD_TO_DEG = 180 / Math.PI;
    private static QuarSk instance;
    private int events;
    private int conditions;
    private int effects;
    private int expressions;

    public void onEnable() {
        if (getServer().getBukkitVersion().startsWith("1.7")) {
            getLogger().log(Level.INFO, "");
            getServer().getPluginManager().disablePlugin(this);
        }
        instance = this;
        normalRegister();
        getLogger().log(Level.INFO, "Registered " + events + " events, " + conditions + " conditions, " + effects + " effects and " + expressions + " expressions ! Good game !");
    }

    private void normalRegister() {
        registerAddon(this);
        /*
         * TYPES
         */
        //Potions
        Classes.registerClass(new ClassInfo<>(PotionEffect.class, "potioneffect")
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
        //Banners
        Classes.registerClass(new ClassInfo<>(Pattern.class, "bannerlayer")
            .name("banner layer")
            .description("A getter for banner layers")
            .parser(new Parser<Pattern>() {
                @Override
                public Pattern parse(String s, ParseContext parseContext) {
                    return null;
                }

                @Override
                public String toString(Pattern pattern, int i) {
                    return "layer with pattern " + EnumType.toString(pattern.getPattern()) + " and color " + pattern.getColor().toString().toLowerCase();
                }

                @Override
                public String toVariableNameString(Pattern pattern) {
                    return "bannerlayer," + pattern.getPattern().toString().toLowerCase() + "," + pattern.getColor().toString().toLowerCase();
                }

                @Override
                public String getVariableNamePattern() {
                    return ".+";
                }
            })
        );
        new EnumType(PatternType.class, "bannerpattern", "banner ?pattern(?: ?type)?");
        /*
         * EFFECTS
         */
        //Orientation
        newEffect(EffOrientTowards.class, "orient %livingentity% (0¦towards|1¦away from) %location%", "make %livingentity% (face|look [at]) (0¦[towards]|1¦away from) %location%", "force %livingentity% to (face|look [at]) (0¦[towards]|1¦away from) %location%");
        //References
        newEffect(EffLinkReference.class, "link @<\\S+> to %object%");
        newEffect(EffUnlinkReference.class, "unlink @<\\S+>");
        newEffect(EffApplyPotionEffects.class, "apply [potion] [effect[s] [of]] %potioneffects% to %livingentities%");
        newEffect(EffMilkEntity.class, "milk %livingentities%");
        /*
         * CONDITIONS
         */
        //Potions
        newCondition(CondHasPotionEffect.class, "[entity] %livingentity% (0¦has [got]|1¦has( not|n't) [got]) [(the|a)] %potioneffecttype% [potion] effect");
        /*
         * EXPRESSIONS
         */
        //References
        newExpression(SExprReference.class, Object.class, ExpressionType.SIMPLE, "@<\\S+>");
        //Potions
        newExpression(ExprCustomPotionEffect.class, PotionEffect.class, ExpressionType.COMBINED, "[[potion] effect [(with|by)]] %potioneffecttype% for %timespan% with [a] [tier [of]] %number% [particles %-boolean%[ with ambient [effect] %-boolean%[ and [particle] colo[u]r[ed] %-color%]]]]]");
        newExpression(ExprCustomPotionItem.class, ItemStack.class, ExpressionType.COMBINED, "(0¦[normal] potion|1¦splash potion|2¦linger[ing] potion|3¦(potion|tipped) arrow) (of|by|with|from|using) [effect[s]] %potioneffects%");
        newExpression(ExprEntityPotionEffects.class, PotionEffect.class, ExpressionType.COMBINED, "[(all|every|each)] [active] [potion] effects (on|in) %livingentities%", "[(every|all|each) of] %livingentities%['s] [active] [potion] effect[s]");
        newExpression(SExprPotionItemEffects.class, PotionEffect.class, ExpressionType.COMBINED, "[(all|every|each)] [potion] effect[s] (on|of) %itemstack%", "[(all|every|each) of] %itemstack%['s] [potion] effect[s]");
        newExpression(ExprPotionEffectType.class, PotionEffectType.class, ExpressionType.COMBINED, "potion[ ]effect[[ ]type][s] of %potioneffect%", "%potioneffect%['s] potion[ ]effect[[ ]type][s]");
        newExpression(ExprPotionEffectDuration.class, Timespan.class, ExpressionType.COMBINED, "(duration|length) of [potion] effect[s] %potioneffect%", "[potion] effect[s] %potioneffect%['s] (duration|length)");
        newExpression(ExprPotionEffectTier.class, Number.class, ExpressionType.COMBINED, "(tier|amplifier) of [potion] [effect] %potioneffect%", "[potion] [effect] %potioneffect%['s] (tier|amplifier)");
        newExpression(SExprItemEffectTypeAmplifier.class, Number.class, ExpressionType.COMBINED, "(tier|amplifier) of [[potion] effect [type]] %potioneffecttype% on [item] %itemstack%", "[[potion] effect [type]] %potioneffecttype%['s] (tier|amplifier) on [item] %itemstack%");
        newExpression(SExprItemEffectTypeDuration.class, Timespan.class, ExpressionType.COMBINED, "(duration|length) of [[potion] effect [type]] %potioneffecttype% on [item] %itemstack%", "[[potion] effect [type]] %potioneffecttype%['s] (duration|length) on [item] %itemstack%");
        //Beacons
        newExpression(ExprEntitiesInRange.class, LivingEntity.class, ExpressionType.COMBINED, "[(all|every|each)] ([living] entit(ies|y)|player[s]) in range of %block%");
        newExpression(ExprBeaconTier.class, Number.class, ExpressionType.COMBINED, "beacon (tier|level) of %block%", "%block%['s] beacon (tier|level)");
        newExpression(SExprBeaconEffects.class, PotionEffect.class, ExpressionType.COMBINED, "[the] (0¦(first|primary)|1¦second[ary]) [potion] effect of [beacon] %block%", "[beacon] %block%['s] (0¦(first|primary)|1¦second[ary]) [potion] effect");
        //Banners
        newExpression(ExprCustomBannerLayer.class, Pattern.class, ExpressionType.COMBINED, "[new] [banner] layer (with|using|of|from) pattern [type] %bannerpattern% colo[u]r[ed] %color%");
        newExpression(ExprBannerOrShieldWithLayers.class, ItemStack.class, ExpressionType.COMBINED, "[new] (0¦banner|1¦shield) (from|with|using|of) [[banner] patterns] %bannerlayers%");
        newExpression(SExprBannerBlockLayers.class, Pattern.class, ExpressionType.COMBINED, "[(all|each|every)] [banner] layer[s] of [(block|banner)] %block%", "[(all|every|each) of] %block%['s] [banner] layers");
        newExpression(SExprItemLayers.class, Pattern.class, ExpressionType.COMBINED, "[(all|each|every)] [banner] layer[s] of [(shield|banner|item)] %itemstack%", "[(all|every|each) of] %itemstack%['s] [banner] layers");
        newExpression(SExprItemBaseColor.class, Color.class, ExpressionType.COMBINED, "[(banner|shield)] bas(e|ic) color of item %itemstack%", "item %itemstack%['s] [(banner|shield)] bas(e|ic) color");
        newExpression(SExprBannerBlockBaseColor.class, Color.class, ExpressionType.COMBINED, "[banner] block bas(e|ic) color of block %block%", "block %block%['s] [banner] bas(e|ic) color");
    }

    public void onDisable() {

    }

    private <E extends Expression<T>, T> void newExpression(Class<E> clazz, Class<T> returnType, ExpressionType type, String... syntaxes) {
        Skript.registerExpression(clazz, returnType, type, prefixEachString(getSyntaxPrefix(), syntaxes));
        expressions++;
    }

    private <E extends Effect> void newEffect(Class<E> clazz, String... syntaxes) {
        Skript.registerEffect(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        effects++;
    }

    private <C extends Condition> void newCondition(Class<C> clazz, String... syntaxes) {
        Skript.registerCondition(clazz, prefixEachString(getSyntaxPrefix(), syntaxes));
        conditions++;
    }

    private <E extends SkriptEvent> void newEvent(Class<E> c, Class<? extends Event> eventClass, String name, String... syntaxes){
        Skript.registerEvent(name, c, eventClass, prefixEachString(getSyntaxPrefix(), syntaxes));
        events++;
    }

    //Potions

    //Conventions
    public String getPrefix() {
        return "[QuarSk] ";
    }

    public String getSyntaxPrefix() {
        return "[quar[s]k] ";
    }

    public String[] prefixEachString(String prefix, String... strings) {
        List<String> stringList = new ArrayList<>();
        for (String str : strings) {
            stringList.add(prefix + str);
        }
        return stringList.toArray(new String[strings.length]);
    }

    public String[] suffixEachString(String suffix, String... strings) {
        List<String> stringList = new ArrayList<>();
        for (String str : strings) {
            stringList.add(str + suffix);
        }
        return stringList.toArray(new String[strings.length]);
    }

    public static QuarSk getInstance() {
        return instance;
    }

    public String getVersion() {
        return "1.1.3";
    }

    //All of the below functions are by bi0qaw cause I honestly can't do that kind of math. Thanks to you.
    public Vector vectorFromLocations(Location from, Location to) {
        return new Vector(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
    }

    public float getYaw(Vector vector) {
        if (((Double) vector.getX()).equals((double) 0) && ((Double) vector.getZ()).equals((double) 0)) {
            return 0;
        }
        return (float) (Math.atan2(vector.getZ(), vector.getX()) * RAD_TO_DEG);
    }

    public float getPitch(Vector vector) {
        double xy = Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
        return (float) (Math.atan(vector.getY() / xy) * RAD_TO_DEG);
    }

    public float notchYaw(float yaw) {
        float y = yaw - 90;
        if (y < -180) {
            y += 360;
        }
        return y;
    }

    public float notchPitch(float pitch) {
        return -pitch;
    }

    public void debug(Object... l) {
        for (Object o : l) {
            Bukkit.getLogger().log(Level.INFO, getPrefix() + " " + o.toString());
        }
    }

}

