package dev.hawu.plugins.api.title;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import dev.hawu.plugins.api.reflect.SimpleLookup;
import dev.hawu.plugins.api.reflect.UncheckedHandles;
import dev.hawu.plugins.api.reflect.UncheckedReflects;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

// This may be one of the worst things I've ever written.
// Wait until you see the NBT Impl.
public final class TitlePacketAdapterImpl extends TitlePacketAdapter {

    private static final TitlePacketAdapterImpl INSTANCE = new TitlePacketAdapterImpl();

    /*
    The classes needed to make this adapter work cross-version.
     */
    private static final Class<?> I_CHAT_BASE_COMPONENT_CLASS;
    private static final Class<?> I_CHAT_MUTABLE_COMPONENT_CLASS;
    private static final Class<?> CHAT_SERIALIZER_CLASS;
    private static final Class<?> PACKET_PLAY_OUT_TITLE;
    private static final Class<?> PACKET_PLAY_OUT_CHAT;
    private static final Class<?> ENUM_TITLE_ACTION;

    /*
    The enum for the titles needed for legacy versions.
     */
    private static final Object TITLE_ENUM_TITLE;
    private static final Object TITLE_ENUM_SUBTITLE;
    private static final Object TITLE_ENUM_CLEAR;
    private static final Object TITLE_ENUM_ACTIONBAR;

    /*
    Method handles for functions and constructors.
     */
    private static final MethodHandle CHAT_CONSTRUCTOR;
    private static final MethodHandle BUKKIT_SEND_TITLE;
    private static final MethodHandle BUKKIT_RESET_TITLE;
    private static final MethodHandle SERIALIZER_A;
    private static final MethodHandle TITLE_TIMES_CONSTRUCTOR;
    private static final MethodHandle TITLE_CONSTRUCTOR;

    /*
    Random stupid things because of 1.17
     */
    private static final Class<?> PACKET_ACTION_BAR;
    private static final Class<?> PACKET_TITLE_ANIMATIONS;
    private static final MethodHandle TITLE_ANIMATIONS_SET;
    private static final MethodHandle ACTION_BAR_SET;

    static {
        final Lookup lookup = MethodHandles.lookup();

        // Look up all classes necessary for sending titles and chat messages.
        I_CHAT_BASE_COMPONENT_CLASS = SimpleLookup.lookupNMS("IChatBaseComponent", "network.chat.IChatBaseComponent");
        I_CHAT_MUTABLE_COMPONENT_CLASS = SimpleLookup.lookupNMS("IChatMutableComponent", "network.chat.IChatMutableComponent");
        PACKET_PLAY_OUT_TITLE = SimpleLookup.lookupNMSOrNull("PacketPlayOutTitle"); // null on v1_17_R1
        PACKET_PLAY_OUT_CHAT = SimpleLookup.lookupNMSOrNull("PacketPlayOutChat"); // null on v1_17_R1
        if(MinecraftVersion.getCurrent() == MinecraftVersion.v1_8_R1) {
            CHAT_SERIALIZER_CLASS = SimpleLookup.lookupNMS("ChatSerializer");
            ENUM_TITLE_ACTION = SimpleLookup.lookupNMS("EnumTitleAction");
        } else {
            ENUM_TITLE_ACTION = SimpleLookup.lookupNMSOrNull("PacketPlayOutTitle$EnumTitleAction"); // null on v1_17_R1
            CHAT_SERIALIZER_CLASS = SimpleLookup.lookupNMS("IChatBaseComponent$ChatSerializer", "network.chat.IChatBaseComponent$ChatSerializer");
        }

        // Retrieve EnumTitleAction.
        // All of these will be null on v1_17_R1
        TITLE_ENUM_TITLE = lookupTitleEnum(lookup, "TITLE");
        TITLE_ENUM_SUBTITLE = lookupTitleEnum(lookup, "SUBTITLE");
        TITLE_ENUM_CLEAR = lookupTitleEnum(lookup, "CLEAR");
        TITLE_ENUM_ACTIONBAR = lookupTitleEnum(lookup, "ACTIONBAR"); // null on v1_10_R1 and below

        // Retrieve methods.
        if(PACKET_PLAY_OUT_CHAT != null) {
            final MethodHandle directHandle = UncheckedHandles.findConstructor(lookup, PACKET_PLAY_OUT_CHAT,
                MethodType.methodType(void.class, I_CHAT_BASE_COMPONENT_CLASS, byte.class));

            // This should only be non-null if the version is from 1.8 - 1.10, as they don't have
            // an ACTIONBAR enum yet.
            if(directHandle != null)
                CHAT_CONSTRUCTOR = directHandle.asType(MethodType.methodType(Object.class, Object.class, byte.class));
            else CHAT_CONSTRUCTOR = null;
        } else CHAT_CONSTRUCTOR = null;

        BUKKIT_SEND_TITLE = UncheckedHandles.findVirtual(lookup, Player.class, "sendTitle",
            MethodType.methodType(void.class, String.class, String.class, int.class, int.class, int.class));
        BUKKIT_RESET_TITLE = UncheckedHandles.findVirtual(lookup, Player.class, "resetTitle", MethodType.methodType(void.class));

        if(MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_18_R1)) {
            SERIALIZER_A = UncheckedHandles.findStatic(lookup, I_CHAT_BASE_COMPONENT_CLASS, "a",
                    MethodType.methodType(I_CHAT_BASE_COMPONENT_CLASS, String.class))
                .asType(MethodType.methodType(Object.class, String.class));
        } else if(MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_16_R1)) {
            SERIALIZER_A = UncheckedHandles.findStatic(lookup, CHAT_SERIALIZER_CLASS, "a",
                    MethodType.methodType(I_CHAT_MUTABLE_COMPONENT_CLASS, String.class))
                .asType(MethodType.methodType(Object.class, String.class));
        } else {
            SERIALIZER_A = UncheckedHandles.findStatic(lookup, CHAT_SERIALIZER_CLASS, "a",
                    MethodType.methodType(I_CHAT_BASE_COMPONENT_CLASS, String.class))
                .asType(MethodType.methodType(Object.class, String.class));
        }

        if(PACKET_PLAY_OUT_TITLE != null) {
            TITLE_TIMES_CONSTRUCTOR = UncheckedHandles.findConstructor(lookup, PACKET_PLAY_OUT_TITLE,
                    MethodType.methodType(void.class, int.class, int.class, int.class))
                .asType(MethodType.methodType(Object.class, int.class, int.class, int.class));
            TITLE_CONSTRUCTOR = UncheckedHandles.findConstructor(lookup, PACKET_PLAY_OUT_TITLE,
                    MethodType.methodType(void.class, ENUM_TITLE_ACTION, I_CHAT_BASE_COMPONENT_CLASS))
                .asType(MethodType.methodType(Object.class, Object.class, Object.class));
        } else {
            TITLE_TIMES_CONSTRUCTOR = null;
            TITLE_CONSTRUCTOR = null;
        }

        // Retrieve 1.17-specific stuff
        PACKET_ACTION_BAR = SimpleLookup.lookupNMSOrNull("", "network.protocol.game.ClientboundSetActionBarTextPacket");
        PACKET_TITLE_ANIMATIONS = SimpleLookup.lookupNMSOrNull("", "network.protocol.game.ClientboundSetTitlesAnimationPacket");
        if(PACKET_TITLE_ANIMATIONS != null && PACKET_ACTION_BAR != null) {
            TITLE_ANIMATIONS_SET = UncheckedHandles.findConstructor(lookup, PACKET_TITLE_ANIMATIONS,
                    MethodType.methodType(void.class, int.class, int.class, int.class))
                .asType(MethodType.methodType(Object.class, int.class, int.class, int.class));
            ACTION_BAR_SET = UncheckedHandles.findConstructor(lookup, PACKET_ACTION_BAR,
                    MethodType.methodType(void.class, I_CHAT_BASE_COMPONENT_CLASS))
                .asType(MethodType.methodType(Object.class, Object.class));
        } else {
            TITLE_ANIMATIONS_SET = null;
            ACTION_BAR_SET = null;
        }
    }

    private TitlePacketAdapterImpl() {}

    @Nullable
    private static Object lookupTitleEnum(final @NotNull Lookup lookup, final @NotNull String name) {
        try {
            return UncheckedHandles.findStaticGetter(lookup, ENUM_TITLE_ACTION, name, ENUM_TITLE_ACTION)
                .asType(MethodType.methodType(Object.class))
                .invokeExact();
        } catch(final Throwable throwable) {
            return null;
        }
    }

    @NotNull
    public static TitlePacketAdapterImpl getInstance() {
        return INSTANCE;
    }

    @NotNull
    private Object wrap(final @NotNull String text, final boolean noEscape, final boolean json) throws Throwable {
        String toWrap = noEscape ? text : Strings.color(StringEscapeUtils.escapeJava(text));
        if(json) return SERIALIZER_A.invokeExact("{\"text\":\"" + toWrap + "\"}");
        else return SERIALIZER_A.invokeExact(toWrap);
    }

    @NotNull
    private Object wrap(final @NotNull String text, final boolean noEscape) throws Throwable {
        return wrap(text, noEscape, true);
    }

    @NotNull
    private Object wrapEmpty() throws Throwable {
        return wrap(" ", true);
    }

    private void sendSetTitleAnimations(final @NotNull Player player, final int fadeIn, final int stay, final int fadeOut) throws Throwable {
        assert TITLE_ANIMATIONS_SET != null;
        final Object times = TITLE_ANIMATIONS_SET.invokeExact(fadeIn, stay, fadeOut);
        UncheckedReflects.sendPacket(player, times);
    }

    private void sendActionBar_v1_18_R1(final @NotNull Player player, final @NotNull TitleComponent component) throws Throwable {
        assert ACTION_BAR_SET != null;
        final Object packet = ACTION_BAR_SET.invokeExact(wrap(component.getTitle(), component.shouldNotWrap(), false));
        sendSetTitleAnimations(player, (int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
        UncheckedReflects.sendPacket(player, packet);
    }

    private void sendActionBar_v1_17_R1(final @NotNull Player player, final @NotNull TitleComponent component) throws Throwable {
        assert ACTION_BAR_SET != null;
        final Object packet = ACTION_BAR_SET.invokeExact(wrap(component.getTitle(), component.shouldNotWrap()));
        sendSetTitleAnimations(player, (int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
        UncheckedReflects.sendPacket(player, packet);
    }

    private void sendActionBar_v1_11_R1(final @NotNull Player player, final @NotNull TitleComponent component) throws Throwable {
        assert TITLE_CONSTRUCTOR != null;
        assert TITLE_TIMES_CONSTRUCTOR != null;
        final Object packet = TITLE_CONSTRUCTOR.invokeExact(TITLE_ENUM_ACTIONBAR, wrap(component.getTitle(), component.shouldNotWrap()));
        final Object times = TITLE_TIMES_CONSTRUCTOR.invokeExact((int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
        UncheckedReflects.sendPacket(player, times);
        UncheckedReflects.sendPacket(player, packet);
    }

    private void sendActionBar_v1_8_R1(final @NotNull Player player, final @NotNull TitleComponent component) throws Throwable {
        assert CHAT_CONSTRUCTOR != null;
        final Object packet = CHAT_CONSTRUCTOR.invokeExact(wrap(component.getTitle(), component.shouldNotWrap()), (byte) 2);
        UncheckedReflects.sendPacket(player, packet);
    }

    @Override
    public void send(final @NotNull Player player, final @NotNull TitleComponent component) {
        try {
            if(BUKKIT_SEND_TITLE != null && MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_12_R1)) {
                BUKKIT_SEND_TITLE.invokeExact(player, Strings.color(component.getTitle()), Strings.color(component.getSubtitle()),
                    (int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
                return;
            }

            assert TITLE_TIMES_CONSTRUCTOR != null;
            assert TITLE_CONSTRUCTOR != null;
            final Object times = TITLE_TIMES_CONSTRUCTOR.invokeExact((int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
            final Object title = TITLE_CONSTRUCTOR.invokeExact(TITLE_ENUM_TITLE, wrap(component.getTitle(), component.shouldNotWrap()));
            final Object subtitle = TITLE_CONSTRUCTOR.invokeExact(TITLE_ENUM_SUBTITLE, wrap(component.getSubtitle(), component.shouldNotWrap()));
            UncheckedReflects.sendPacket(player, times);
            UncheckedReflects.sendPacket(player, title);
            UncheckedReflects.sendPacket(player, subtitle);
        } catch(Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendActionBar(final @NotNull Player player, final @NotNull TitleComponent component) {
        try {
            switch(MinecraftVersion.getCurrent()) {
                case v1_11_R1:
                case v1_12_R1:
                case v1_13_R1:
                case v1_13_R2:
                case v1_14_R1:
                case v1_15_R1:
                case v1_16_R1:
                case v1_16_R2:
                case v1_16_R3:
                    sendActionBar_v1_11_R1(player, component);
                    break;
                case v1_17_R1:
                    sendActionBar_v1_17_R1(player, component);
                    break;
                case v1_18_R1:
                    sendActionBar_v1_18_R1(player, component);
                    break;
                default:
                    sendActionBar_v1_8_R1(player, component);
                    break;
            }
        } catch(final Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @Override
    public void clear(final @NotNull Player player) {
        try {
            if(BUKKIT_RESET_TITLE != null && MinecraftVersion.getCurrent().isAtLeast(MinecraftVersion.v1_16_R1)) {
                BUKKIT_RESET_TITLE.invokeExact(player);
                return;
            }

            assert TITLE_CONSTRUCTOR != null;
            final Object reset = TITLE_CONSTRUCTOR.invokeExact(TITLE_ENUM_CLEAR, wrapEmpty());
            UncheckedReflects.sendPacket(player, reset);
        } catch(final Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

}
