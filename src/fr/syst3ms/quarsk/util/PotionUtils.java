package fr.syst3ms.quarsk.util;

import com.sun.istack.internal.NotNull;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;

/**
 * Created by PRODSEB on 27/01/2017.
 */
@SuppressWarnings("unused")
public class PotionUtils {
    private static final PotionUtils instance = new PotionUtils();

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
        return (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION || item.getType() == Material.TIPPED_ARROW);
    }

    public PotionEffect fromPotionData(PotionData data) {
        PotionEffectType type = data.getType().getEffectType();
        return type == PotionEffectType.HEAL || type == PotionEffectType.HARM ? data.isUpgraded()
                ? new PotionEffect(type, 1, 2) : new PotionEffect(type, 1, 1)
                : type == PotionEffectType.REGENERATION || type == PotionEffectType.POISON ? data.isExtended()
                        ? new PotionEffect(type, 1800, 1)
                        : data.isUpgraded() ? new PotionEffect(type, 440, 2) : new PotionEffect(type, 900, 1)
                        : type == PotionEffectType.NIGHT_VISION || type == PotionEffectType.INVISIBILITY || type == PotionEffectType.FIRE_RESISTANCE || type == PotionEffectType.WATER_BREATHING
                                ? data.isExtended() ? new PotionEffect(type, 9600, 1) : new PotionEffect(type, 3600, 1)
                                : type == PotionEffectType.WEAKNESS || type == PotionEffectType.SLOW ? data.isExtended()
                                        ? new PotionEffect(type, 4800, 1) : new PotionEffect(type, 1800, 1)
                                        : data.isExtended() ? new PotionEffect(type, 9600, 1)
                                                : data.isUpgraded() ? new PotionEffect(type, 1800, 2)
                                                        : new PotionEffect(type, 3600, 1);
    }

    public PotionData emptyPotionData() {
        return new PotionData(PotionType.WATER);
    }

    public boolean isEntityThrownPotion(Entity entity) {
        return entity instanceof ThrownPotion;
    }

    public PotionMeta emptyPotionMeta() {
        ItemStack item = new ItemStack(Material.POTION);
        return (PotionMeta) item.getItemMeta();
    }
}
