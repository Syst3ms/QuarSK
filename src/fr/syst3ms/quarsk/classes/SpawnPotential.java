package fr.syst3ms.quarsk.classes;

import fr.syst3ms.quarsk.Quarsk;

/**
 * Created by ARTHUR on 06/03/2017.
 */
@SuppressWarnings("unused")
public class SpawnPotential {
    private String type;
    private short weight;
    private Object nbt;

    public SpawnPotential(String type) {
        this(type, (short) 0);
    }

    public SpawnPotential(String type, short weight) {
        this(type, weight, null);
    }

    public SpawnPotential(String type, short weight, Object nbt) {
        this.setType(type);
        this.setWeight(weight);
        this.setNbt(nbt);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
}
