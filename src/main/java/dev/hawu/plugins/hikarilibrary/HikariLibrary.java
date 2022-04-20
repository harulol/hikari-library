package dev.hawu.plugins.hikarilibrary;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.chat.ChatPacketAdapter;
import dev.hawu.plugins.api.gui.GuiClickEvents;
import dev.hawu.plugins.api.impl.ChatPacketAdapterImpl;
import dev.hawu.plugins.api.impl.WorldEntitiesLookupAdapterImpl;
import dev.hawu.plugins.api.inventories.Inventories;
import dev.hawu.plugins.api.items.BukkitMaterial;
import dev.hawu.plugins.api.misc.WorldEntitiesLookupAdapter;
import dev.hawu.plugins.api.particles.ParticlePacketAdapter;
import dev.hawu.plugins.api.impl.ParticlePacketAdapterImpl;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import dev.hawu.plugins.api.title.TitlePacketAdapter;
import dev.hawu.plugins.api.impl.TitlePacketAdapterImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HikariLibrary extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitMaterial.initialize();
        GuiClickEvents.initialize(this);
        Inventories.setPlugin(this);

        getLogger().info("Loading adapters for Minecraft " + MinecraftVersion.getCurrent().name());

        TitlePacketAdapter.setAdapter(TitlePacketAdapterImpl.getInstance());
        ParticlePacketAdapter.setAdapter(ParticlePacketAdapterImpl.getInstance());
        ChatPacketAdapter.setAdapter(ChatPacketAdapterImpl.INSTANCE);
        WorldEntitiesLookupAdapter.setAdapter(WorldEntitiesLookupAdapterImpl.getInstance());
    }

    @Override
    public void onDisable() {
        GuiClickEvents.onDisable();
        Tasks.cancelAllTasks(this);
        Bukkit.getOnlinePlayers().forEach(GuiClickEvents::safelyClose);
    }

}
