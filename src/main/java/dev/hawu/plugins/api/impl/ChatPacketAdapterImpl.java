package dev.hawu.plugins.api.impl;

import dev.hawu.plugins.api.chat.ChatPacketAdapter;
import dev.hawu.plugins.api.chat.TextComponent;
import dev.hawu.plugins.api.reflect.LookupException;
import dev.hawu.plugins.api.reflect.SimpleLookup;
import dev.hawu.plugins.api.reflect.UncheckedHandles;
import dev.hawu.plugins.api.reflect.UncheckedReflects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.UUID;

public final class ChatPacketAdapterImpl extends ChatPacketAdapter {

    public static final ChatPacketAdapterImpl INSTANCE = new ChatPacketAdapterImpl();

    // Static objects
    private static final Object CHAT_MESSAGE_TYPE_CHAT;

    // Method handles
    private static final MethodHandle THREE_CONSTRUCTOR; // Constructor with 3 params in later versions
    private static final MethodHandle ONE_CONSTRUCTOR;
    private static final MethodHandle SERIALIZER;

    static {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();

        final Class<?> CHAT_SERIALIZER = SimpleLookup.findNMS("IChatBaseComponent$ChatSerializer", "network.chat.IChatBaseComponent$ChatSerializer")
            .orElse(SimpleLookup.lookupNMSOrNull("ChatSerializer"));
        final Class<?> I_CHAT_BASE_COMPONENT = SimpleLookup.lookupNMS("IChatBaseComponent", "network.chat.IChatBaseComponent");
        final Class<?> I_CHAT_MUTABLE_COMPONENT = SimpleLookup.lookupNMSOrNull("IChatMutableComponent", "network.chat.IChatMutableComponent");
        final Class<?> PACKET_PLAY_OUT_CHAT = SimpleLookup.lookupNMS("PacketPlayOutChat", "network.protocol.game.PacketPlayOutChat");
        final Class<?> CHAT_MESSAGE_TYPE = SimpleLookup.findNMS("ChatMessageType", "network.chat.ChatMessageType").orNull();

        if(CHAT_MESSAGE_TYPE != null) {
            CHAT_MESSAGE_TYPE_CHAT = UncheckedHandles.findStaticGetter(lookup, CHAT_MESSAGE_TYPE, "CHAT", CHAT_MESSAGE_TYPE)
                .either(UncheckedHandles.findStaticGetter(lookup, CHAT_MESSAGE_TYPE, "a", CHAT_MESSAGE_TYPE))
                .map(handle -> {
                    try {
                        return handle.asType(UncheckedHandles.methodType(Object.class).get()).invokeExact();
                    } catch(Throwable e) {
                        throw new LookupException(e);
                    }
                })
                .orNull();
            THREE_CONSTRUCTOR = UncheckedHandles.findConstructor(lookup, PACKET_PLAY_OUT_CHAT,
                    UncheckedHandles.methodType(void.class, I_CHAT_BASE_COMPONENT, CHAT_MESSAGE_TYPE, UUID.class).get())
                .map(handle -> handle.asType(UncheckedHandles.methodType(Object.class, Object.class, Object.class, UUID.class).get()))
                .orNull();
        } else {
            CHAT_MESSAGE_TYPE_CHAT = null;
            THREE_CONSTRUCTOR = null;
        }

        ONE_CONSTRUCTOR = UncheckedHandles.findConstructor(lookup, PACKET_PLAY_OUT_CHAT, UncheckedHandles.methodType(void.class, I_CHAT_BASE_COMPONENT).get())
            .map(handle -> handle.asType(UncheckedHandles.methodType(Object.class, Object.class).get()))
            .orNull();
        SERIALIZER = UncheckedHandles.findStatic(lookup, CHAT_SERIALIZER, "a", UncheckedHandles.methodType(I_CHAT_BASE_COMPONENT, String.class).orNull())
            .either(UncheckedHandles.findStatic(lookup, CHAT_SERIALIZER, "a", UncheckedHandles.methodType(I_CHAT_MUTABLE_COMPONENT, String.class).orNull()))
            .map(handle -> handle.asType(UncheckedHandles.methodType(Object.class, String.class).get()))
            .get();
    }

    private ChatPacketAdapterImpl() {}

    private void sendPacket(final Player player, final String text) {
        try {
            final Object serializedComponent = SERIALIZER.invokeExact(text);
            if(THREE_CONSTRUCTOR != null)
                UncheckedReflects.sendPacket(player, THREE_CONSTRUCTOR.invokeExact(serializedComponent, CHAT_MESSAGE_TYPE_CHAT, player.getUniqueId()));
            else UncheckedReflects.sendPacket(player, ONE_CONSTRUCTOR.invokeExact(serializedComponent));
        } catch(final Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull TextComponent component) {
        sendPacket(player, component.toString());
    }

    @Override
    public void sendAll(final @NotNull TextComponent component) {
        Bukkit.getOnlinePlayers().forEach(p -> sendPlayer(p, component));
    }

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull String message) {
        sendPacket(player, "{\"text\":\"" + message + "\"}");
    }

    @Override
    public void sendAll(final @NotNull String message) {
        Bukkit.getOnlinePlayers().forEach(p -> sendPlayer(p, message));
    }

}
