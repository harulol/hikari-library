package dev.hawu.plugins.api.v1_18_R1;

import dev.hawu.plugins.api.chat.ChatPacketAdapter;
import dev.hawu.plugins.api.chat.TextComponent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("DuplicatedCode")
@Internal
public final class SimpleChatPacketAdapter extends ChatPacketAdapter {

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull TextComponent component) {
        player.spigot().sendMessage(ComponentSerializer.parse(component.toString()));
    }

    @Override
    public void sendAll(final @NotNull TextComponent component) {
        final BaseComponent[] baseComponents = ComponentSerializer.parse(component.toString());
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(baseComponents));
    }

    @Override
    public void sendPlayer(final @NotNull Player player, final @NotNull String message) {
        final String msg = String.format("{\"text\":\"%s\"}", StringEscapeUtils.escapeJava(message));
        player.spigot().sendMessage(ComponentSerializer.parse(msg));
    }

    @Override
    public void sendAll(final @NotNull String message) {
        final String msg = String.format("{\"text\":\"%s\"}", StringEscapeUtils.escapeJava(message));
        final BaseComponent[] baseComponents = ComponentSerializer.parse(msg);
        Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(baseComponents));
    }

}
