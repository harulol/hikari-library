package dev.hawu.plugins.api.v1_8_R2;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.adapters.TitlePacketAdapter;
import dev.hawu.plugins.api.title.TitleComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle.EnumTitleAction;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Internal
public final class SimpleTitlePacketAdapter extends TitlePacketAdapter {

    private IChatBaseComponent wrap(final String string) {
        return ChatSerializer.a("{\"text\":\"" + Strings.color(StringEscapeUtils.escapeJava(string)) + "\"}");
    }

    @Override
    public void send(final @NotNull Player player, final @NotNull TitleComponent component) {
        final PacketPlayOutTitle[] packets = new PacketPlayOutTitle[3];
        packets[0] = new PacketPlayOutTitle((int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
        packets[1] = new PacketPlayOutTitle(EnumTitleAction.TITLE, wrap(component.getTitle()));
        packets[2] = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, wrap(component.getSubtitle()));
        Arrays.stream(packets).forEach(packet -> ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet));
    }

    @Override
    public void sendActionBar(final @NotNull Player player, final @NotNull TitleComponent component) {
        final PacketPlayOutChat actionBar = new PacketPlayOutChat(wrap(component.getTitle()), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionBar);
    }

    @Override
    public void clear(final @NotNull Player player) {
        final PacketPlayOutTitle clear = new PacketPlayOutTitle(EnumTitleAction.CLEAR, wrap(" "));
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(clear);
    }

}
