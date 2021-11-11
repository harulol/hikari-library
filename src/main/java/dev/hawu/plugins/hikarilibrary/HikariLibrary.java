package dev.hawu.plugins.hikarilibrary;

import dev.hawu.plugins.api.Tasks;
import org.bukkit.plugin.java.JavaPlugin;

public final class HikariLibrary extends JavaPlugin {

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        Tasks.cancelAllTasks(this);
    }

}
