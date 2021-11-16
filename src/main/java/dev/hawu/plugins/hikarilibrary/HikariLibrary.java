package dev.hawu.plugins.hikarilibrary;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.gui.GuiClickEvents;
import dev.hawu.plugins.api.items.BukkitMaterial;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HikariLibrary extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitMaterial.initialize();
        GuiClickEvents.initialize(this);
    }

    @Override
    public void onDisable() {
        Tasks.cancelAllTasks(this);
        Bukkit.getOnlinePlayers().forEach(GuiClickEvents::safelyClose);
    }

}
