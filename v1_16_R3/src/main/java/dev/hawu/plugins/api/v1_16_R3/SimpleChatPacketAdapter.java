package dev.hawu.plugins.api.v1_16_R3;

import dev.hawu.plugins.api.adapters.ChatPacketAdapter;
import dev.hawu.plugins.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@SuppressWarnings("DuplicatedCode")
@Internal
public final class SimpleChatPacketAdapter extends ChatPacketAdapter {

    private PacketPlayOutChat getPacket(final @Nullable UUID uuid, final @NotNull String message) {
        return new PacketPlayOutChat(ChatSerializer.a(message), ChatMessageType.CHAT, uuid);
    }

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull TextComponent component) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(getPacket(player.getUniqueId(), component.toString()));
    }

    @Override
    public void sendAll(final @NotNull TextComponent component) {
        final PacketPlayOutChat chat = getPacket(null, component.toString());
        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(chat));
    }

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull String message) {
        final PacketPlayOutChat chat = getPacket(player.getUniqueId(), "{\"text\":\"" + StringEscapeUtils.escapeJava(message) + "\"}");
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
    }

    @Override
    public void sendAll(final @NotNull String message) {
        final PacketPlayOutChat chat = getPacket(UUID.randomUUID(), "{\"text\":\"" + StringEscapeUtils.escapeJava(message) + "\"}");
        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(chat));
    }

}
