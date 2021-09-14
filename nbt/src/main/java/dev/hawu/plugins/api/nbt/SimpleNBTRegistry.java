package dev.hawu.plugins.api.nbt;

import dev.hawu.plugins.api.reflect.MinecraftVersion;
import dev.hawu.plugins.api.reflect.SimpleLookup;
import dev.hawu.plugins.api.reflect.UncheckedHandles;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// I really hate this class.
final class SimpleNBTRegistry extends NBTRegistry {

    // Classes
    private static final Class<?> TAG_BASE;
    private static final Class<?> TAG_BYTE;
    private static final Class<?> TAG_SHORT;
    private static final Class<?> TAG_INT;
    private static final Class<?> TAG_LONG;
    private static final Class<?> TAG_FLOAT;
    private static final Class<?> TAG_DOUBLE;
    private static final Class<?> TAG_STRING;
    private static final Class<?> TAG_BYTE_ARRAY;
    private static final Class<?> TAG_INT_ARRAY;
    private static final Class<?> TAG_LONG_ARRAY;
    private static final Class<?> TAG_LIST;
    private static final Class<?> TAG_COMPOUND;

    private static final Class<?> CRAFT_ITEM_STACK;
    private static final Class<?> NMS_ITEM_STACK;

    // Constructors
    private static final MethodHandle TAG_BYTE_CONSTRUCTOR;
    private static final MethodHandle TAG_SHORT_CONSTRUCTOR;
    private static final MethodHandle TAG_INT_CONSTRUCTOR;
    private static final MethodHandle TAG_LONG_CONSTRUCTOR;
    private static final MethodHandle TAG_FLOAT_CONSTRUCTOR;
    private static final MethodHandle TAG_DOUBLE_CONSTRUCTOR;
    private static final MethodHandle TAG_STRING_CONSTRUCTOR;
    private static final MethodHandle TAG_BYTE_ARRAY_CONSTRUCTOR;
    private static final MethodHandle TAG_INT_ARRAY_CONSTRUCTOR;
    private static final MethodHandle TAG_LONG_ARRAY_CONSTRUCTOR;
    private static final MethodHandle TAG_LIST_CONSTRUCTOR;
    private static final MethodHandle TAG_COMPOUND_CONSTRUCTOR;

    // Getters
    private static final MethodHandle TAG_BYTE_GETTER;
    private static final MethodHandle TAG_SHORT_GETTER;
    private static final MethodHandle TAG_INT_GETTER;
    private static final MethodHandle TAG_LONG_GETTER;
    private static final MethodHandle TAG_FLOAT_GETTER;
    private static final MethodHandle TAG_DOUBLE_GETTER;
    private static final MethodHandle TAG_STRING_GETTER;
    private static final MethodHandle TAG_BYTE_ARRAY_GETTER;
    private static final MethodHandle TAG_INT_ARRAY_GETTER;
    private static final MethodHandle TAG_LONG_ARRAY_GETTER;
    private static final MethodHandle TAG_LIST_GETTER;
    private static final MethodHandle TAG_COMPOUND_GETTER;

    // Other methods.
    private static final MethodHandle TAG_LIST_ADD_METHOD;
    private static final MethodHandle TAG_COMPOUND_SET_METHOD;

    private static final MethodHandle CRAFT_ITEM_AS_NMS_METHOD;
    private static final MethodHandle CRAFT_ITEM_AS_BUKKIT_METHOD;
    private static final MethodHandle NMS_ITEM_GET_TAG_METHOD;
    private static final MethodHandle NMS_ITEM_SET_TAG_METHOD;

    static {
        // I'm aware that reflections will be way more error-prone than abstractions.
        // But for now, I can't think of a good model for abstraction, so this has to do for now.

        // Initialize classes.
        TAG_BASE = SimpleLookup.lookupNMS("NBTBase", "nbt.NBTBase");
        TAG_BYTE = SimpleLookup.lookupNMS("NBTTagByte", "nbt.NBTTagByte");
        TAG_SHORT = SimpleLookup.lookupNMS("NBTTagShort", "nbt.NBTTagShort");
        TAG_INT = SimpleLookup.lookupNMS("NBTTagInt", "nbt.NBTTagInt");
        TAG_LONG = SimpleLookup.lookupNMS("NBTTagLong", "nbt.NBTTagLong");
        TAG_FLOAT = SimpleLookup.lookupNMS("NBTTagFloat", "nbt.NBTTagFloat");
        TAG_DOUBLE = SimpleLookup.lookupNMS("NBTTagDouble", "nbt.NBTTagDouble");
        TAG_STRING = SimpleLookup.lookupNMS("NBTTagString", "nbt.NBTTagString");
        TAG_BYTE_ARRAY = SimpleLookup.lookupNMS("NBTTagByteArray", "nbt.NBTTagByteArray");
        TAG_INT_ARRAY = SimpleLookup.lookupNMS("NBTTagIntArray", "nbt.NBTTagIntArray");
        TAG_LONG_ARRAY = SimpleLookup.lookupNMSOrNull("NBTTagLongArray", "nbt.NBTTagLongArray");
        TAG_LIST = SimpleLookup.lookupNMS("NBTTagList", "nbt.NBTTagList");
        TAG_COMPOUND = SimpleLookup.lookupNMS("NBTTagCompound", "nbt.NBTTagCompound");
        CRAFT_ITEM_STACK = SimpleLookup.lookupOBC("inventory.CraftItemStack");
        NMS_ITEM_STACK = SimpleLookup.lookupNMS("ItemStack", "world.item.ItemStack");

        // Initialize constructors.
        TAG_BYTE_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_BYTE, MethodType.methodType(Void.TYPE, Byte.TYPE));
        TAG_SHORT_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_SHORT, MethodType.methodType(Void.TYPE, Short.TYPE));
        TAG_INT_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_INT, MethodType.methodType(Void.TYPE, Integer.TYPE));
        TAG_LONG_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_LONG, MethodType.methodType(Void.TYPE, Long.TYPE));
        TAG_FLOAT_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_FLOAT, MethodType.methodType(Void.TYPE, Float.TYPE));
        TAG_DOUBLE_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_DOUBLE, MethodType.methodType(Void.TYPE, Double.TYPE));
        TAG_STRING_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_STRING, MethodType.methodType(Void.TYPE, String.class));
        TAG_BYTE_ARRAY_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_BYTE_ARRAY, MethodType.methodType(Void.TYPE, byte[].class));
        TAG_INT_ARRAY_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_INT_ARRAY, MethodType.methodType(Void.TYPE, int[].class));
        TAG_LIST_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_LIST, MethodType.methodType(Void.TYPE));
        TAG_COMPOUND_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_COMPOUND, MethodType.methodType(Void.TYPE));

        // Initialize getters.
        final boolean newVersion = MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_17_R1);

        // Because Minecraft decides to just not name the fields "data" since 1.17 because who the hell knows.
        TAG_BYTE_GETTER = UncheckedHandles.findGetter(TAG_BYTE, newVersion ? "x" : "data", Byte.TYPE);

        // Why is the data field in the byte tag named "x" but "c" for others??
        TAG_SHORT_GETTER = UncheckedHandles.findGetter(TAG_SHORT, newVersion ? "c" : "data", Short.TYPE);
        TAG_INT_GETTER = UncheckedHandles.findGetter(TAG_INT, newVersion ? "c" : "data", Integer.TYPE);
        TAG_LONG_GETTER = UncheckedHandles.findGetter(TAG_LONG, newVersion ? "c" : "data", Long.TYPE);
        TAG_BYTE_ARRAY_GETTER = UncheckedHandles.findGetter(TAG_BYTE_ARRAY, newVersion ? "c" : "data", byte[].class);
        TAG_INT_ARRAY_GETTER = UncheckedHandles.findGetter(TAG_INT_ARRAY, newVersion ? "c" : "data", int[].class);

        // What the fuck?
        TAG_FLOAT_GETTER = UncheckedHandles.findGetter(TAG_FLOAT, newVersion ? "w" : "data", Float.TYPE);
        TAG_DOUBLE_GETTER = UncheckedHandles.findGetter(TAG_DOUBLE, newVersion ? "w" : "data", Double.TYPE);

        // Ok.
        TAG_STRING_GETTER = UncheckedHandles.findGetter(TAG_STRING, newVersion ? "A" : "data", String.class);
        TAG_LIST_GETTER = UncheckedHandles.findGetter(TAG_LIST, newVersion ? "c" : "list", List.class);
        TAG_COMPOUND_GETTER = UncheckedHandles.findGetter(TAG_COMPOUND, newVersion ? "x" : "map", Map.class);

        // Only initialize respective constructor and getter if the tag exists.
        if(TAG_LONG_ARRAY != null) {
            TAG_LONG_ARRAY_CONSTRUCTOR = UncheckedHandles.findConstructor(TAG_LONG_ARRAY, MethodType.methodType(Void.TYPE, long[].class));
            TAG_LONG_ARRAY_GETTER = UncheckedHandles.findGetter(TAG_LONG_ARRAY, newVersion ? "c" : "data", long[].class);
        } else {
            TAG_LONG_ARRAY_CONSTRUCTOR = null;
            TAG_LONG_ARRAY_GETTER = null;
        }

        // Initialize methods that may be needed.
        TAG_LIST_ADD_METHOD = UncheckedHandles.findVirtual(TAG_LIST, "add", MethodType.methodType(Void.TYPE, TAG_BASE));
        if(MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_14_R1)) {
            TAG_COMPOUND_SET_METHOD = UncheckedHandles.findVirtual(TAG_COMPOUND, "set", MethodType.methodType(TAG_BASE, Integer.TYPE, TAG_BASE));
        } else {
            TAG_COMPOUND_SET_METHOD = UncheckedHandles.findVirtual(TAG_COMPOUND, "set", MethodType.methodType(Void.TYPE, Integer.TYPE, TAG_BASE));
        }

        CRAFT_ITEM_AS_NMS_METHOD = UncheckedHandles.findStatic(CRAFT_ITEM_STACK, "asNMSCopy", MethodType.methodType(NMS_ITEM_STACK, ItemStack.class));
        CRAFT_ITEM_AS_BUKKIT_METHOD = UncheckedHandles.findStatic(CRAFT_ITEM_STACK, "asBukkitCopy", MethodType.methodType(ItemStack.class, NMS_ITEM_STACK));
        NMS_ITEM_GET_TAG_METHOD = UncheckedHandles.findVirtual(NMS_ITEM_STACK, "getTag", MethodType.methodType(TAG_COMPOUND));
        NMS_ITEM_SET_TAG_METHOD = UncheckedHandles.findVirtual(NMS_ITEM_STACK, "setTag", MethodType.methodType(Void.TYPE, TAG_COMPOUND));
    }

    @NotNull
    private NBTList convertNMSList(@NotNull final Object obj) throws Throwable {
        final List<?> objects = (List<?>) Objects.requireNonNull(TAG_LIST_GETTER).invokeExact(obj);
        final NBTList list = new NBTList();
        for(final Object tag : objects) {
            final NBTType nbt = convertNMSBase(tag);
            if(nbt != null) list.add(nbt);
        }
        return list;
    }

    @NotNull
    private NBTCompound convertNMSCompound(@NotNull final Object obj) throws Throwable {
        final Map<?, ?> map = (Map<?, ?>) Objects.requireNonNull(TAG_COMPOUND_GETTER).invokeExact(obj);
        final NBTCompound compound = new NBTCompound();
        for(final Map.Entry<?, ?> entry : map.entrySet()) {
            final NBTType nbt = convertNMSBase(entry.getValue());
            if(entry.getKey() instanceof String && nbt != null) compound.put((String) entry.getKey(), (NBTType) entry.getValue());
        }
        return compound;
    }

    @Nullable
    private NBTType convertNMSBase(@Nullable final Object obj) throws Throwable {
        if(TAG_BYTE.isInstance(obj)) {
            return new NBTByte((Byte) Objects.requireNonNull(TAG_BYTE_GETTER).invokeExact(obj));
        } else if(TAG_SHORT.isInstance(obj)) {
            return new NBTShort((Short) Objects.requireNonNull(TAG_SHORT_GETTER).invokeExact(obj));
        } else if(TAG_INT.isInstance(obj)) {
            return new NBTInt((Integer) Objects.requireNonNull(TAG_INT_GETTER).invokeExact(obj));
        } else if(TAG_LONG.isInstance(obj)) {
            return new NBTLong((Long) Objects.requireNonNull(TAG_LONG_GETTER).invokeExact(obj));
        } else if(TAG_FLOAT.isInstance(obj)) {
            return new NBTFloat((Float) Objects.requireNonNull(TAG_FLOAT_GETTER).invokeExact(obj));
        } else if(TAG_DOUBLE.isInstance(obj)) {
            return new NBTDouble((Double) Objects.requireNonNull(TAG_DOUBLE_GETTER).invokeExact(obj));
        } else if(TAG_STRING.isInstance(obj)) {
            return new NBTString((String) Objects.requireNonNull(TAG_STRING_GETTER).invokeExact(obj));
        } else if(TAG_BYTE_ARRAY.isInstance(obj)) {
            return new NBTByteArray((byte[]) Objects.requireNonNull(TAG_BYTE_ARRAY_GETTER).invokeExact(obj));
        } else if(TAG_INT_ARRAY.isInstance(obj)) {
            return new NBTIntArray((int[]) Objects.requireNonNull(TAG_INT_ARRAY_GETTER).invokeExact(obj));
        } else if(TAG_LONG_ARRAY != null && TAG_LONG_ARRAY.isInstance(obj)) { // Making sure the tag exists since long arrays only exist from v1.12
            return new NBTLongArray((long[]) Objects.requireNonNull(TAG_LONG_ARRAY_GETTER).invokeExact(obj));
        } else if(TAG_LIST.isInstance(obj)) {
            return convertNMSList(obj);
        } else if(TAG_COMPOUND.isInstance(obj)) {
            return convertNMSCompound(obj);
        } else {
            return null;
        }
    }

    @NotNull
    private Object convertAPIList(@NotNull final NBTList tag) throws Throwable {
        final Object list = Objects.requireNonNull(TAG_LIST_CONSTRUCTOR).invokeExact();
        for(final NBTType type : tag) {
            final Object nbt = convertAPIBase(type);
            if(nbt != null) Objects.requireNonNull(TAG_LIST_ADD_METHOD).invokeExact(list, nbt);
        }
        return list;
    }

    @NotNull
    private Object convertAPICompound(@NotNull final NBTCompound tag) throws Throwable {
        final Object compound = Objects.requireNonNull(TAG_COMPOUND_CONSTRUCTOR).invokeExact();
        for(final Map.Entry<String, NBTType> entry : tag.entrySet()) {
            final Object nbt = convertAPIBase(entry.getValue());
            if(nbt != null) Objects.requireNonNull(TAG_COMPOUND_SET_METHOD).invokeExact(entry.getKey(), nbt);
        }
        return compound;
    }

    @Nullable
    private Object convertAPIBase(@Nullable final NBTType type) throws Throwable {
        if(type instanceof NBTByte) {
            return Objects.requireNonNull(TAG_BYTE_CONSTRUCTOR).invokeExact(((NBTByte) type).getData());
        } else if(type instanceof NBTShort) {
            return Objects.requireNonNull(TAG_SHORT_CONSTRUCTOR).invokeExact(((NBTShort) type).getData());
        } else if(type instanceof NBTInt) {
            return Objects.requireNonNull(TAG_INT_CONSTRUCTOR).invokeExact(((NBTInt) type).getData());
        } else if(type instanceof NBTLong) {
            return Objects.requireNonNull(TAG_LONG_CONSTRUCTOR).invokeExact(((NBTLong) type).getData());
        } else if(type instanceof NBTFloat) {
            return Objects.requireNonNull(TAG_FLOAT_CONSTRUCTOR).invokeExact(((NBTFloat) type).getData());
        } else if(type instanceof NBTDouble) {
            return Objects.requireNonNull(TAG_DOUBLE_CONSTRUCTOR).invokeExact(((NBTDouble) type).getData());
        } else if(type instanceof NBTString) {
            return Objects.requireNonNull(TAG_STRING_CONSTRUCTOR).invokeExact(((NBTString) type).getData());
        } else if(type instanceof NBTByteArray) {
            return Objects.requireNonNull(TAG_BYTE_ARRAY_CONSTRUCTOR).invokeExact(((NBTByteArray) type).getData());
        } else if(type instanceof NBTIntArray) {
            return Objects.requireNonNull(TAG_INT_ARRAY_CONSTRUCTOR).invokeExact(((NBTIntArray) type).getData());
        } else if(type instanceof NBTLongArray && TAG_LONG_ARRAY_CONSTRUCTOR != null) {
            return TAG_LONG_ARRAY_CONSTRUCTOR.invokeExact(((NBTLongArray) type).getData());
        } else if(type instanceof NBTList) {
            return convertAPIList((NBTList) type);
        } else if(type instanceof NBTCompound) {
            return convertAPICompound((NBTCompound) type);
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public NBTCompound getCompound(@NotNull final ItemStack item) {
        try {
            final Object nmsCopy = Objects.requireNonNull(CRAFT_ITEM_AS_NMS_METHOD).invokeExact(item);
            final Object tag = Objects.requireNonNull(NMS_ITEM_GET_TAG_METHOD).invokeExact(nmsCopy);

            if(tag == null) return null;
            else return convertNMSCompound(tag);
        } catch(final Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Override
    @NotNull
    public ItemStack applyCompound(@NotNull final ItemStack item, @Nullable final NBTCompound compound) {
        try {
            final Object nmsCopy = Objects.requireNonNull(CRAFT_ITEM_AS_NMS_METHOD).invokeExact(item);
            Objects.requireNonNull(NMS_ITEM_SET_TAG_METHOD).invokeExact(nmsCopy, compound == null ? null : convertAPICompound(compound));
            return (ItemStack) Objects.requireNonNull(CRAFT_ITEM_AS_BUKKIT_METHOD).invokeExact(nmsCopy);
        } catch(final Throwable throwable) {
            throw new Error(throwable);
        }
    }

}
