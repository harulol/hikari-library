package dev.hawu.plugins.api.reflect;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * A utility helper class for finding constructors, methods
 * and fields from classes reflectively without the need
 * of {@code try-catch} blocks due to checked exceptions.
 *
 * @since 1.0
 */
public final class UncheckedReflects {

    private static final Class<?> CRAFT_PLAYER_CLASS;
    private static final Class<?> ENTITY_PLAYER_CLASS;
    private static final Class<?> PLAYER_CONNECTION_CLASS;
    private static final Class<?> PACKET_CLASS;

    private static final MethodHandle GET_HANDLE;
    private static final MethodHandle PLAYER_CONNECTION;
    private static final MethodHandle SEND_PACKET;

    static {
        CRAFT_PLAYER_CLASS = SimpleLookup.lookupOBC("entity.CraftPlayer");
        ENTITY_PLAYER_CLASS = SimpleLookup.lookupNMS("EntityPlayer", "server.level.EntityPlayer");
        PLAYER_CONNECTION_CLASS = SimpleLookup.lookupNMS("PlayerConnection", "server.network.PlayerConnection");
        PACKET_CLASS = SimpleLookup.lookupNMS("Packet", "network.protocol.Packet");

        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        final String playerConnectionFieldName;
        final String sendPacketMethodName;

        switch(MinecraftVersion.getCurrent()) {
            case v1_17_R1:
                playerConnectionFieldName = "b";
                sendPacketMethodName = "sendPacket";
                break;
            case v1_18_R1:
                playerConnectionFieldName = "b";
                sendPacketMethodName = "a";
                break;
            default:
                sendPacketMethodName = "sendPacket";
                playerConnectionFieldName = "playerConnection";
                break;
        }

        GET_HANDLE = UncheckedHandles.findVirtual(lookup, CRAFT_PLAYER_CLASS, "getHandle", MethodType.methodType(ENTITY_PLAYER_CLASS))
            .map(handle -> handle.asType(MethodType.methodType(Object.class, Object.class)))
            .orElse(null);
        PLAYER_CONNECTION = UncheckedHandles.findGetter(lookup, ENTITY_PLAYER_CLASS, playerConnectionFieldName, PLAYER_CONNECTION_CLASS)
            .map(handle -> handle.asType(MethodType.methodType(Object.class, Object.class)))
            .orElse(null);
        SEND_PACKET = UncheckedHandles.findVirtual(lookup, PLAYER_CONNECTION_CLASS, sendPacketMethodName,
                MethodType.methodType(void.class, PACKET_CLASS))
            .map(handle -> handle.asType(MethodType.methodType(void.class, Object.class, Object.class)))
            .orElse(null);
    }

    /**
     * Attempts to retrieve a {@link Class} by name.
     *
     * @param name The name of the class.
     * @return A {@link Class} if found, {@code null} otherwise.
     * @since 1.3
     */
    @NotNull
    public static Optional<Class<?>> getClass(final @NotNull String name) {
        try {
            return Optional.of(Class.forName(name));
        } catch(final Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Attempts to retrieve a {@link Constructor}.
     *
     * @param cls      The class that has the constructor.
     * @param declared Whether the constructor is declared.
     * @param types    The parameter types for the constructor.
     * @return A {@link Constructor} if found, {@code null} otherwise.
     * @since 1.0
     */
    @NotNull
    public static Optional<Constructor<?>> getConstructor(final @NotNull Class<?> cls, final boolean declared, final @NotNull Class<?> @NotNull ... types) {
        try {
            if(declared)
                return Optional.of(cls.getDeclaredConstructor(types));
            else return Optional.of(cls.getConstructor(types));
        } catch(final NoSuchMethodException ex) {
            return Optional.empty();
        }
    }

    /**
     * Attempts to retrieve a {@link Method}.
     *
     * @param cls      The class that has the method.
     * @param declared Whether the method is declared.
     * @param name     The name of the method.
     * @param params   The parameter types of the method.
     * @return A {@link Method} if found, {@code null} otherwise.
     * @since 1.0
     */
    @NotNull
    public static Optional<Method> getMethod(final @NotNull Class<?> cls, final boolean declared, final @NotNull String name, final @NotNull Class<?> @NotNull ... params) {
        try {
            if(declared)
                return Optional.of(cls.getDeclaredMethod(name, params));
            else return Optional.of(cls.getMethod(name, params));
        } catch(final NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    /**
     * Attempts to retrieve a {@link Field}.
     *
     * @param cls      The class that has the field.
     * @param declared Whether the field is declared by cls.
     * @param name     The name of the field.
     * @return A {@link Field} if found, {@code null} otherwise.
     * @since 1.0
     */
    @NotNull
    public static Optional<Field> getField(final @NotNull Class<?> cls, final boolean declared, final @NotNull String name) {
        try {
            if(declared)
                return Optional.of(cls.getDeclaredField(name));
            else return Optional.of(cls.getField(name));
        } catch(final NoSuchFieldException ex) {
            return Optional.empty();
        }
    }

    /**
     * Attempts to look up a class, or throws an exception
     * if it was not found.
     *
     * @param name The name of the class.
     * @return The class.
     * @since 1.3
     */
    @NotNull
    public static Class<?> getClassOrThrow(final @NotNull String name) {
        try {
            return Class.forName(name);
        } catch(final Exception exception) {
            throw new LookupException(exception);
        }
    }

    /**
     * Attempts to retrieve a {@link Constructor}.
     *
     * @param cls      The class that has the constructor.
     * @param declared Whether the constructor is declared.
     * @param types    The parameter types for the constructor.
     * @return A {@link Constructor} if found.
     * @throws LookupException If the constructor is not found.
     * @since 1.0
     */
    @NotNull
    public static Constructor<?> getConstructorOrThrow(final @NotNull Class<?> cls, final boolean declared, final @NotNull Class<?> @NotNull ... types) {
        try {
            if(declared)
                return cls.getDeclaredConstructor(types);
            else return cls.getConstructor(types);
        } catch(final NoSuchMethodException ex) {
            throw new LookupException(ex);
        }
    }

    /**
     * Attempts to retrieve a {@link Method}.
     *
     * @param cls      The class that has the method.
     * @param declared Whether the method is declared.
     * @param name     The name of the method.
     * @param params   The parameter types for the method.
     * @return A {@link Method} if found.
     * @throws LookupException If the method is not found.
     * @since 1.0
     */
    @NotNull
    public static Method getMethodOrThrow(final @NotNull Class<?> cls, final boolean declared, final @NotNull String name, final @NotNull Class<?> @NotNull ... params) {
        try {
            if(declared)
                return cls.getDeclaredMethod(name, params);
            else return cls.getMethod(name, params);
        } catch(final NoSuchMethodException e) {
            throw new LookupException(e);
        }
    }

    /**
     * Attempts to retrieve a {@link Field}.
     *
     * @param cls      The class that has the field.
     * @param declared Whether the field is declared by cls.
     * @param name     The name of the field.
     * @return A {@link Field} if found.
     * @throws LookupException If the method is not found.
     * @since 1.0
     */
    @NotNull
    public static Field getFieldOrThrow(final @NotNull Class<?> cls, final boolean declared, final @NotNull String name) {
        try {
            if(declared)
                return cls.getDeclaredField(name);
            else return cls.getField(name);
        } catch(final NoSuchFieldException ex) {
            throw new LookupException(ex);
        }
    }

    /**
     * Sends a packet to a player using method handles. This should
     * be theoretically as fast as direct access and faster than reflections
     * and lambda metafactory.
     *
     * @param player The player to send to.
     * @param packet The packet to send.
     * @since 1.3
     */
    public static void sendPacket(final @NotNull Player player, final @NotNull Object packet) {
        try {
            final Object craftPlayer = CRAFT_PLAYER_CLASS.cast(player);
            final Object entityPlayer = GET_HANDLE.invokeExact(craftPlayer);
            final Object playerConnection = PLAYER_CONNECTION.invokeExact(entityPlayer);
            SEND_PACKET.invokeExact(playerConnection, packet);
        } catch(final Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
