package fr.syst3ms.quarsk.util;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.runtime.regexp.joni.constants.EncloseType;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by PRODSEB on 27/01/2017.
 */
@SuppressWarnings("unused")
public class PotionUtils {
    private static final PotionUtils instance = new PotionUtils();

    private PotionUtils() {}

    public static PotionUtils getInstance() {
        return instance;
    }

    public PotionEffect getEffectByEffectType(@NotNull PotionMeta meta, @NotNull PotionEffectType effectType) {
        List<PotionEffect> effectList = meta.getCustomEffects();
        for (PotionEffect effect : effectList) {
            if (effect.getType() == effectType) {
                return effect;
            }
        }
        return null;
    }

    public boolean isPotionItem(ItemStack item) {
        return (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION);
    }

    //I know I could have used ternary operators, but it makes the code much less readable
    public PotionEffect fromPotionData(PotionData data) {
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

    public PotionData emptyPotionData() {
        return new PotionData(PotionType.WATER);
    }

    public boolean isEntityThrownPotion(Entity entity) {
        return entity.getType() == EntityType.SPLASH_POTION || entity.getType() == EntityType.LINGERING_POTION;
    }

    public PotionMeta emptyPotionMeta() {
        ItemStack item = new ItemStack(Material.POTION);
        return (PotionMeta) item.getItemMeta();
    }
}
