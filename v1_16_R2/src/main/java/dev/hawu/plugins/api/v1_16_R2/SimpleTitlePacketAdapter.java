package dev.hawu.plugins.api.v1_16_R2;

import dev.hawu.plugins.api.title.TitleComponent;
import dev.hawu.plugins.api.title.TitlePacketAdapter;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.Packet;
import net.minecraft.server.v1_16_R2.PacketPlayOutTitle;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simple adapter for creating and sending title components
 * for version {@code v1_16_R2}.
 *
 * @since 1.0
 */
@Deprecated
@SuppressWarnings("DuplicatedCode")
public final class SimpleTitlePacketAdapter extends TitlePacketAdapter {

    @NotNull
    private static String color(final @NotNull String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Nullable
    private static IChatBaseComponent wrap(final @NotNull String s) {
        return IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + color(StringEscapeUtils.escapeJava(s)) + "}");
    }

    private static void sendPacket(final @NotNull Player player, final @NotNull Packet<?> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void send(final @NotNull Player player, final @NotNull TitleComponent component) {
        player.sendTitle(color(component.getTitle()), color(component.getSubtitle()),
            (int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
    }

    @Override
    public void sendActionBar(final @NotNull Player player, final @NotNull TitleComponent component) {
        final Packet<?> times = new PacketPlayOutTitle((int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut());
        final Packet<?> packet = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, wrap(component.getTitle()));
        sendPacket(player, times);
        sendPacket(player, packet);
    }

    @Override
    public void clear(final @NotNull Player player) {
        player.resetTitle();
    }

}
