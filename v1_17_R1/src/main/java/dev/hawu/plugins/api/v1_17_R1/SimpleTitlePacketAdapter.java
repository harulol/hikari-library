package dev.hawu.plugins.api.v1_17_R1;

import dev.hawu.plugins.api.Strings;
import dev.hawu.plugins.api.adapters.TitlePacketAdapter;
import dev.hawu.plugins.api.title.TitleComponent;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
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
        final ClientboundSetTitlesAnimationPacket times = new ClientboundSetTitlesAnimationPacket(
            (int) component.getFadeIn(), (int) component.getStay(), (int) component.getFadeOut()
        );
        final ClientboundSetActionBarTextPacket actionBar = new ClientboundSetActionBarTextPacket(wrap(Strings.color(component.getTitle())));
        ((CraftPlayer) player).getHandle().b.sendPacket(times);
        ((CraftPlayer) player).getHandle().b.sendPacket(actionBar);
    }

    @Override
    public void clear(final @NotNull Player player) {
        player.resetTitle();
    }

}
