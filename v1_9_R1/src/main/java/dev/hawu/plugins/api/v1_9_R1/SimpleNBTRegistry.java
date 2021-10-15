package dev.hawu.plugins.api.v1_9_R1;

import dev.hawu.plugins.api.nbt.*;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Extension for the NBT registry for Bukkit v1_9_R1.
 *
 * @deprecated See {@link NBTRegistry#getRegistry()}
 */
@SuppressWarnings("DuplicatedCode")
@Deprecated
public final class SimpleNBTRegistry extends NBTRegistry {

    @NotNull
    private NBTTagList transformAPIList(@NotNull final NBTList list) {
        final NBTTagList tag = new NBTTagList();
        list.forEach(b -> {
            final NBTBase base = transformAPIBase(b);
            if(base != null) tag.add(base);
        });
        return tag;
    }

    @NotNull
    private NBTTagCompound transformAPICompound(@NotNull final NBTCompound compound) {
        final NBTTagCompound tag = new NBTTagCompound();
        compound.forEach((k, v) -> {
            final NBTBase base = transformAPIBase(v);
            if(base != null) tag.set(k, base);
        });
        return tag;
    }

    @Nullable
    private NBTBase transformAPIBase(@NotNull final NBTType type) {
        if(type instanceof NBTByte) {
            return new NBTTagByte(((NBTByte) type).getData());
        } else if(type instanceof NBTShort) {
            return new NBTTagShort(((NBTShort) type).getData());
        } else if(type instanceof NBTInt) {
            return new NBTTagInt(((NBTInt) type).getData());
        } else if(type instanceof NBTLong) {
            return new NBTTagLong(((NBTLong) type).getData());
        } else if(type instanceof NBTFloat) {
            return new NBTTagFloat(((NBTFloat) type).getData());
        } else if(type instanceof NBTDouble) {
            return new NBTTagDouble(((NBTDouble) type).getData());
        } else if(type instanceof NBTString) {
            return new NBTTagString(((NBTString) type).getData());
        } else if(type instanceof NBTByteArray) {
            return new NBTTagByteArray(((NBTByteArray) type).getData());
        } else if(type instanceof NBTIntArray) {
            return new NBTTagIntArray(((NBTIntArray) type).getData());
        } else if(type instanceof NBTList) {
            return transformAPIList((NBTList) type);
        } else if(type instanceof NBTCompound) {
            return transformAPICompound((NBTCompound) type);
        } else return null;
    }

    @NotNull
    private NBTList transformNMSList(@NotNull final NBTTagList list) {
        final NBTList tag = new NBTList();
        for(int i = 0; i < list.size(); i++) {
            final NBTType nbt = transformNMSBase(list.h(i));
            if(nbt != null) tag.add(nbt);
        }
        return tag;
    }

    @NotNull
    private NBTCompound transformNMSCompound(@NotNull final NBTTagCompound compound) {
        final NBTCompound tag = new NBTCompound();
        for(final String obj : compound.c()) {
            if(obj == null || compound.get(obj) == null) continue;
            final NBTType nbt = transformNMSBase(compound.get(obj));
            if(nbt != null) tag.put(obj, nbt);
        }
        return tag;
    }

    @Nullable
    private NBTType transformNMSBase(@NotNull final NBTBase base) {
        if(base instanceof NBTTagByte) {
            return new NBTByte(((NBTTagByte) base).f());
        } else if(base instanceof NBTTagShort) {
            return new NBTShort(((NBTTagShort) base).e());
        } else if(base instanceof NBTTagInt) {
            return new NBTInt(((NBTTagInt) base).d());
        } else if(base instanceof NBTTagLong) {
            return new NBTLong(((NBTTagLong) base).c());
        } else if(base instanceof NBTTagFloat) {
            return new NBTFloat(((NBTTagFloat) base).h());
        } else if(base instanceof NBTTagDouble) {
            return new NBTDouble(((NBTTagDouble) base).g());
        } else if(base instanceof NBTTagString) {
            return new NBTString(((NBTTagString) base).a_());
        } else if(base instanceof NBTTagByteArray) {
            return new NBTByteArray(((NBTTagByteArray) base).c());
        } else if(base instanceof NBTTagIntArray) {
            return new NBTIntArray(((NBTTagIntArray) base).c());
        } else if(base instanceof NBTTagList) {
            return transformNMSList((NBTTagList) base);
        } else if(base instanceof NBTTagCompound) {
            return transformNMSCompound((NBTTagCompound) base);
        } else return null;
    }

    @Override
    public @Nullable NBTCompound getCompound(final @NotNull ItemStack item) {
        final net.minecraft.server.v1_9_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(item);
        if(nmsCopy.hasTag())
            return transformNMSCompound(nmsCopy.getTag());
        else return null;
    }

    @Override
    public @NotNull ItemStack applyCompound(final @NotNull ItemStack item, final @Nullable NBTCompound compound) {
        final net.minecraft.server.v1_9_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(item);
        if(compound != null) nmsCopy.setTag(transformAPICompound(compound));
        else nmsCopy.setTag(null);
        return CraftItemStack.asBukkitCopy(nmsCopy);
    }

}
