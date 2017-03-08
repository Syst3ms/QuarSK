package fr.syst3ms.quarsk.util.nms;

import fr.syst3ms.quarsk.classes.SpawnPotential;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by ARTHUR on 06/03/2017.
 */
public class Nms_1_9_4 implements Nms {
    @Override
    public Object getNbtTag(Object compound, String tag) {
        return compound instanceof NBTTagCompound ? ((NBTTagCompound) compound).get(tag) : null;
    }

    @Override
    public boolean hasTag(Object compound, String tag) {
        return compound instanceof NBTTagCompound && ((NBTTagCompound) compound).hasKey(tag);
    }

    @Override
    public void setNbtTag(Object compound, String tag, Object toSet) {
        if (compound instanceof NBTTagCompound && (toSet instanceof NBTBase || toSet instanceof Number || toSet instanceof String)) {
            NBTBase converted;
            converted = toSet instanceof Number ? (NBTBase) convertToNbt((Number) toSet) :
                    toSet instanceof String ? (NBTBase) convertToNbt((String) toSet) : (NBTBase) toSet;
            ((NBTTagCompound) compound).set(tag, converted);
        }
    }

    @Override
    public void removeNBTTag(Object compound, String tag) {
        if (compound instanceof NBTTagCompound)
            ((NBTTagCompound) compound).remove(tag);
    }

    @Override
    public Object convertToNbt(Number number) {
        return number instanceof Byte ? new NBTTagByte((byte) number) :
                number instanceof Short ? new NBTTagShort((short) number) :
                        number instanceof Integer ? new NBTTagInt((int) number) :
                                number instanceof Long ? new NBTTagLong((long) number) :
                                        number instanceof Float ? new NBTTagFloat((float) number) :
                                                number instanceof Double ? new NBTTagDouble((double) number) : null;
    }

    @Override
    public Object convertToNbt(String string) {
        return new NBTTagString(string);
    }

    @Override
    public Object getBlockNbt(Block block) {
        NBTTagCompound compound = new NBTTagCompound();
        World world = ((CraftWorld) block.getWorld()).getHandle();
        TileEntity tileEntity = world.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
        if (tileEntity == null)
            return null;
        tileEntity.save(compound);
        return compound;
    }

    @Override
    public void setBlockNbt(Block block, Object newCompound) {
        if (newCompound instanceof NBTTagCompound) {
            World nmsWorld = ((CraftWorld) block.getWorld()).getHandle();
            TileEntity tileEntity = nmsWorld.getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            if (tileEntity == null)
                return;
            tileEntity.a((NBTTagCompound) newCompound);
            tileEntity.update();
            IBlockData tileEntType = nmsWorld.getType(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            nmsWorld.notify(tileEntity.getPosition(), tileEntType, tileEntType, 3);
        }
    }

    @Override
    public Object getEntityNbt(org.bukkit.entity.Entity entity) {
        net.minecraft.server.v1_9_R2.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound NBT = new NBTTagCompound();
        nmsEntity.e(NBT);
        return NBT;
    }

    @Override
    public Short getShort(Object compound, String tag) {
        return compound instanceof NBTTagCompound ? ((NBTTagCompound) compound).getShort(tag) : null;
    }

    @Override
    public void setShort(Object compound, String tag, short num) {
        if (compound instanceof NBTTagCompound)
            ((NBTTagCompound) compound).setShort(tag, num);
    }

    @Override
    public Object spawnPotentialNbt(SpawnPotential... spawnPotentials) {
        NBTTagList tagList = new NBTTagList();
        Stream.of(spawnPotentials)
              .map(pot -> {
                    NBTTagCompound compound = new NBTTagCompound();
                    compound.setString("Type", pot.getType());
                    compound.setShort("Weight", pot.getWeight());
                    compound.set("Properties", (NBTTagCompound) pot.getNbt());
                    return compound;
              })
              .forEach(tagList::add);
        return tagList;
    }

    @Override
    public SpawnPotential nbtToSpawnPotential(Object compound) {
        if (compound instanceof NBTTagCompound) {
            NBTTagCompound tagCompound = (NBTTagCompound) compound;
            if (tagCompound.hasKey("Type") && tagCompound.hasKey("Weight") && tagCompound.hasKey("Properties")) {
                return new SpawnPotential(tagCompound.getString("Type"), tagCompound.getShort("Weight"), tagCompound.getCompound("Properties"));
            }
        }
        return null;
    }

    @Override
    public List<Object> getArrayElements(Object compoundList) {
        List<Object> list = new ArrayList<>();
        if (compoundList instanceof NBTTagList) {
            NBTTagList tagList = (NBTTagList) compoundList;
            list = IntStream.range(0, tagList.size()).mapToObj(tagList::get).collect(Collectors.toList());
        }
        return list;
    }
}
