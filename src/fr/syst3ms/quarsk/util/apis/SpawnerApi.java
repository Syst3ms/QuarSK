package fr.syst3ms.quarsk.util.apis;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Math2;
import fr.syst3ms.quarsk.Quarsk;
import fr.syst3ms.quarsk.classes.SpawnPotential;
import fr.syst3ms.quarsk.util.MathUtils;
import fr.syst3ms.quarsk.util.nms.Nms;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;

import java.util.stream.Stream;

/**
 * Created by ARTHUR on 06/03/2017.
 */
@SuppressWarnings("unused")
public class SpawnerApi {
    private static final Nms nms = Quarsk.getNms();

    public static void setSingleSpawnedEntity(Block spawner, Entity entity) {
        if (!(spawner.getState() instanceof CreatureSpawner) )
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner),
               entityNbt = nms.getEntityNbt(entity);
        nms.setNbtTag(blockNbt, "SpawnData", entityNbt);
        nms.removeNBTTag(blockNbt, "SpawnPotentials");
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static void setSpawnedEntities(Block spawner, SpawnPotential... potentials) {
        if (!(spawner.getState() instanceof CreatureSpawner) )
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        nms.removeNBTTag(blockNbt, "EntityId");
        nms.setNbtTag(blockNbt, "SpawnPotentials", nms.spawnPotentialNbt(potentials));
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static SpawnPotential[] getSpawnedEntities(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner) )
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        return Stream.of(nms.getArrayElements(nms.getNbtTag(blockNbt, "SpawnPotentials")))
                     .map(nms::nbtToSpawnPotential)
                     .toArray(SpawnPotential[]::new);
    }

    public static short getSpawnRange(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        return nms.getShort(nms.getBlockNbt(spawner), "SpawnRange");
    }

    public static void setSpawnRange(Block spawner, short range) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        nms.setShort(blockNbt, "SpawnRange", range);
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static short getSpawnCount(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        return nms.getShort(nms.getBlockNbt(spawner), "SpawnCount");
    }

    public static void setSpawnCount(Block spawner, short count) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        nms.setShort(blockNbt, "SpawnCount", count);
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static short getRequiredPlayerRange(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        return nms.getShort(nms.getBlockNbt(spawner), "RequiredPlayerRange");
    }

    public static void setRequiredPlayerRange(Block spawner, short playerRange) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        nms.setShort(blockNbt, "RequiredPlayerRange", playerRange);
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static Timespan getFixedDelay(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        return nms.hasTag(blockNbt, "MinSpawnDelay") && nms.hasTag(blockNbt, "MinSpawnDelay") ?
                new Timespan(MathUtils.mean(nms.getShort(blockNbt, "MinSpawnDelay"), nms.getShort(blockNbt, "MaxSpawnDelay")) * 50) :
                new Timespan(nms.getShort(blockNbt, "Delay") * 50);
    }

    public static void setFixedDelay(Block spawner, Timespan delay) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        if (nms.hasTag(blockNbt, "MinSpawnDelay"))
            nms.removeNBTTag(blockNbt, "MinSpawnDelay");
        if (nms.hasTag(blockNbt, "MaxSpawnDelay"))
            nms.removeNBTTag(blockNbt, "MaxSpawnDelay");
        nms.setShort(blockNbt, "Delay", (short) delay.getTicks_i());
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static Timespan getMinDelay(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        return nms.hasTag(blockNbt, "Delay") ? new Timespan(nms.getShort(blockNbt, "Delay") * 50) :
                new Timespan(nms.getShort(nms.getBlockNbt(spawner), "MinSpawnDelay") * 50);
    }

    public static void setMinDelay(Block spawner, Timespan minDelay) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        if (nms.hasTag(blockNbt, "Delay"))
            nms.removeNBTTag(blockNbt, "Delay");
        if (!nms.hasTag(blockNbt, "MaxSpawnDelay"))
            nms.setShort(blockNbt, "MaxSpawnDelay", (short) minDelay.getTicks_i());
        nms.setShort(blockNbt, "MinSpawnDelay", (short) minDelay.getTicks_i());
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static Timespan getMaxDelay(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        return nms.hasTag(blockNbt, "Delay") ? new Timespan(nms.getShort(blockNbt, "Delay") * 50) :
                new Timespan(nms.getShort(blockNbt, "MaxSpawnDelay") * 50);
    }

    public static void setMaxDelay(Block spawner, Timespan maxDelay) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        if (nms.hasTag(blockNbt, "Delay"))
            nms.removeNBTTag(blockNbt, "Delay");
        if (!nms.hasTag(blockNbt, "MinSpawnDelay"))
            nms.setShort(blockNbt, "MinSpawnDelay", (short) maxDelay.getTicks_i());
        nms.setShort(blockNbt, "MaxSpawnDelay", (short) maxDelay.getTicks_i());
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static short getMaxNearbyEntities(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        return nms.getShort(nms.getBlockNbt(spawner), "MaxNearbyEntities");
    }

    public static void setMaxNearbyEntities(Block spawner, short maxEntities) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        nms.setShort(blockNbt, "MaxNearbyEntities", maxEntities);
        nms.setBlockNbt(spawner, blockNbt);
    }

    public static boolean isSpawningSingleType(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        return nms.hasTag(blockNbt, "EntityId");
    }

    public static boolean hasFixedDelay(Block spawner) {
        if (!(spawner.getState() instanceof CreatureSpawner))
            Skript.exception(new IllegalArgumentException(), "The provided block must be a spawner");
        Object blockNbt = nms.getBlockNbt(spawner);
        return nms.hasTag(blockNbt, "Delay");
    }
}
