package dev.hawu.plugins.api.v1_18_R1;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.adapters.TitlePacketAdapter;
import dev.hawu.plugins.api.title.TitleComponent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
public final class SimpleTitlePacketAdapter extends TitlePacketAdapter {

    private BaseComponent[] parse(final String s) {
        return ComponentSerializer.parse("{\"text\":\"" + Strings.color(StringEscapeUtils.escapeJava(s)) + "\"}");
    }

    @Override
    public void send(final @NotNull Player player, final @NotNull TitleComponent component) {
        player.sendTitle(Strings.color(component.getTitle()), Strings.color(component.getSubtitle()),
            (int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
    }

    @Override
    public void sendActionBar(final @NotNull Player player, final @NotNull TitleComponent component) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, parse(component.getTitle()));
    }

    @Override
    public void clear(final @NotNull Player player) {
        player.resetTitle();
    }

}
