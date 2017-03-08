package fr.syst3ms.quarsk.util.nms;

import fr.syst3ms.quarsk.classes.SpawnPotential;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import java.util.List;

/**
 * Created by ARTHUR on 06/03/2017.
 */
@SuppressWarnings("unused")
public interface Nms {
    Object getNbtTag(Object compound, String tag);

    boolean hasTag(Object compound, String tag);

    void setNbtTag(Object compound, String tag, Object toSet);

    void removeNBTTag(Object compound, String tag);

    Object convertToNbt(Number number);

    Object convertToNbt(String string);

    Object getBlockNbt(Block block);

    void setBlockNbt(Block block, Object newCompound);

    Object getEntityNbt(Entity entity);

    Short getShort(Object compound, String tag);

    void setShort(Object compound, String tag, short num);

    Object spawnPotentialNbt(SpawnPotential... spawnPotentials);

    SpawnPotential nbtToSpawnPotential(Object compound);

    List<Object> getArrayElements(Object compoundList);
}
