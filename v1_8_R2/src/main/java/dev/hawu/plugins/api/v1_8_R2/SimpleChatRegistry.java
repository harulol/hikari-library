package dev.hawu.plugins.api.v1_8_R2;

import dev.hawu.plugins.api.chat.ChatPacketAdapter;
import dev.hawu.plugins.api.chat.TextComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Deprecated
public final class SimpleChatRegistry extends ChatPacketAdapter {

    private void sendPacket(final Player player, final String msg) {
        final IChatBaseComponent component = ChatSerializer.a(msg);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(component));
    }

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull TextComponent component) {
        sendPacket(player, component.toString());
    }

    @Override
    public void sendAll(final @NotNull TextComponent component) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPlayer(player, component));
    }

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull String message) {
        sendPacket(player, "{\"text\":\"" + message + "\"}");
    }

    @Override
    public void sendAll(final @NotNull String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendPlayer(player, message));
    }

}
