package fr.syst3ms.quarsk.classes;

import ch.njol.skript.Skript;
import com.google.common.base.Preconditions;
import fr.syst3ms.quarsk.Quarsk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 * Created by ARTHUR on 06/03/2017.
 */
@SuppressWarnings("unused")
public final class SpawnPotential {
    private EntityType type;
    private short weight;
    private Object nbt;

    public SpawnPotential(Entity entity) {
        this(entity.getType());
    }

    public SpawnPotential(EntityType type) {
        this(type, (short) 0);
        Preconditions.checkArgument(type != EntityType.WEATHER && type != EntityType.COMPLEX_PART, "Invalid entity type %s can't be spawned in a spawner", type);
    }

    public SpawnPotential(Entity entity, short weight) {
        this(entity.getType(), weight, Quarsk.getNms().getEntityNbt(entity));
    }

    public SpawnPotential(EntityType type, short weight) {
        this(type, weight, null);
    }

    public SpawnPotential(EntityType type, short weight, Object nbt) {
        this.setType(type);
        this.setWeight(weight);
        this.setNbt(nbt);
    }

    @SuppressWarnings("deprecation")
    public String getType() {
        if (Skript.isRunningMinecraft(1, 11)) {
            switch (type) {
                case LIGHTNING:
                    return "lightning_bolt";
                case FISHING_HOOK:
                    return "unknown";
                case LINGERING_POTION:
                    return "potion";
                case PLAYER:
                    return "player";
                case TIPPED_ARROW:
                    return "arrow";
                default:
                    return type.getName();
            }
        } else {
            switch (type) {
                case LIGHTNING:
                    return "LightningBolt";
                case FISHING_HOOK:
                    return "unknown";
                case LINGERING_POTION:
                    if (!Skript.isRunningMinecraft(1, 9))
                        throw new IllegalStateException();
                    return "ThrownPotion";
                case PLAYER:
                    return "Player";
                case TIPPED_ARROW:
                    if (!Skript.isRunningMinecraft(1, 9))
                        throw new IllegalStateException();
                    return "Arrow";
                default:
                    return type.getName();
            }
        }
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public short getWeight() {
        return weight;
    }

    public void setWeight(short weight) {
        this.weight = weight;
    }

    public Object getNbt() {
        return nbt;
    }

    public void setNbt(Object nbt) {
        this.nbt = nbt;
    }

    @Override
    public String toString() {
        return "potential " + this.getType() + " with weight " + this.getWeight();
    }
}
