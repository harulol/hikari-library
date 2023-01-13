package dev.hawu.plugins.api.v1_16_R2;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.adapters.TitlePacketAdapter;
import dev.hawu.plugins.api.title.TitleComponent;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R2.PacketPlayOutTitle.EnumTitleAction;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
public final class SimpleTitlePacketAdapter extends TitlePacketAdapter {

    private IChatBaseComponent wrap(final String string) {
        return ChatSerializer.a("{\"text\":\"" + Strings.color(StringEscapeUtils.escapeJava(string)) + "\"}");
    }

    @Override
    public void send(final @NotNull Player player, final @NotNull TitleComponent component) {
        player.sendTitle(Strings.color(component.getTitle()), Strings.color(component.getSubtitle()),
            (int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
    }

    @Override
    public void sendActionBar(final @NotNull Player player, final @NotNull TitleComponent component) {
        final PacketPlayOutTitle times = new PacketPlayOutTitle((int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
        final PacketPlayOutTitle actionBar = new PacketPlayOutTitle(EnumTitleAction.ACTIONBAR, wrap(component.getTitle()));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(times);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionBar);
    }

    @Override
    public void clear(final @NotNull Player player) {
        final PacketPlayOutTitle clear = new PacketPlayOutTitle(EnumTitleAction.CLEAR, wrap(" "));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(clear);
    }

}
