package dev.hawu.plugins.hikarilibrary;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.events.Events;
import dev.hawu.plugins.api.gui.GuiClickEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HikariLibrary extends JavaPlugin {

    @Override
    public void onEnable() {
        Events.registerEvents(this, GuiClickEvents.getInstance());
    }

    @Override
    public void onDisable() {
        Tasks.cancelAllTasks(this);
        Bukkit.getOnlinePlayers().forEach(GuiClickEvents::safelyClose);
    }

}
