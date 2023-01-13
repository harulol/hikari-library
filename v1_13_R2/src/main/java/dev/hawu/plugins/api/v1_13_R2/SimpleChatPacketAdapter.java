package dev.hawu.plugins.api.v1_13_R2;

import dev.hawu.plugins.api.chat.ChatPacketAdapter;
import dev.hawu.plugins.api.chat.TextComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("DuplicatedCode")
@Internal
public final class SimpleChatPacketAdapter extends ChatPacketAdapter {

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull TextComponent component) {
        final PacketPlayOutChat chat = new PacketPlayOutChat(ChatSerializer.a(component.toString()));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
    }

    @Override
    public void sendAll(final @NotNull TextComponent component) {
        final PacketPlayOutChat chat = new PacketPlayOutChat(ChatSerializer.a(component.toString()));
        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(chat));
    }

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull String message) {
        final PacketPlayOutChat chat = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + StringEscapeUtils.escapeJava(message) + "\"}"));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(chat);
    }

    @Override
    public void sendAll(final @NotNull String message) {
        final PacketPlayOutChat chat = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + StringEscapeUtils.escapeJava(message) + "\"}"));
        Bukkit.getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(chat));
    }

}
