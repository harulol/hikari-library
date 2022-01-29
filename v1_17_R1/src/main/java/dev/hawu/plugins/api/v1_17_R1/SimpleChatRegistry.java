package dev.hawu.plugins.api.v1_17_R1;

import dev.hawu.plugins.api.chat.ChatPacketAdapter;
import dev.hawu.plugins.api.chat.TextComponent;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Deprecated
public final class SimpleChatRegistry extends ChatPacketAdapter {

    private void sendPacket(final Player player, final String msg) {
        final IChatBaseComponent component = ChatSerializer.a(msg);
        ((CraftPlayer) player).getHandle().b.sendPacket(new PacketPlayOutChat(component, ChatMessageType.a, player.getUniqueId()));
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
