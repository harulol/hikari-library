package dev.hawu.plugins.api.v1_8_R3;

import dev.hawu.plugins.api.reflect.UncheckedReflects;
import dev.hawu.plugins.api.title.TitleComponent;
import dev.hawu.plugins.api.title.TitlePacketAdapter;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Simple adapter for creating and sending title components
 * for version {@code v1_8_R3}.
 *
 * @since 1.0
 */
@Deprecated
@SuppressWarnings("DuplicatedCode")
public final class SimpleTitlePacketAdapter extends TitlePacketAdapter {

    private static final IChatBaseComponent EMPTY = wrap("");

    @NotNull
    private static String color(final @NotNull String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @NotNull
    private static IChatBaseComponent wrap(final @NotNull String s) {
        return IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + color(StringEscapeUtils.escapeJava(s)) + "\"}");
    }

    private static void sendPacket(final @NotNull Player player, final @NotNull Packet<?> packet) {
        UncheckedReflects.sendPacket(player, packet);
//        final CraftPlayer craftPlayer = (CraftPlayer) player;
//        final EntityPlayer entityPlayer = craftPlayer.getHandle();
//        final PlayerConnection playerConnection = entityPlayer.playerConnection;
//        playerConnection.sendPacket(packet);
    }

    @Override
    public void send(final @NotNull Player player, final @NotNull TitleComponent component) {
        final Packet<?> times = new PacketPlayOutTitle((int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
        final Packet<?> title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, wrap(component.getTitle()));
        final Packet<?> subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, wrap(component.getSubtitle()));
        Stream.of(times, title, subtitle).forEach(p -> sendPacket(player, p));
    }

    @Override
    public void sendActionBar(final @NotNull Player player, final @NotNull TitleComponent component) {
        final Packet<?> packet = new PacketPlayOutChat(wrap(component.getTitle()), (byte) 2);
        sendPacket(player, packet);
    }

    @Override
    public void clear(final @NotNull Player player) {
        final Packet<?> packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, EMPTY);
        sendPacket(player, packet);
    }

}
