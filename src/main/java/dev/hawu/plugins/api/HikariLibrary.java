package dev.hawu.plugins.api;

import dev.hawu.plugins.api.commands.BaseCommand;
import dev.hawu.plugins.api.inventories.ClickEvents;
import dev.hawu.plugins.api.inventories.item.BukkitMaterial;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public final class HikariLibrary extends JavaPlugin {

    private static HikariLibrary instance;
    private static final String authToken = null;

    public static HikariLibrary getInstance() {
        return instance;
    }

    @Nullable
    public static String getAuthToken() {
        return authToken;
    }

    @Override
    public void onEnable() {
        instance = this;
        ClickEvents.init(this);
        BukkitMaterial.reInitDisplayableMaterials(this);
        new BaseCommand(this);
    }

    @Override
    public void onDisable() {
        instance = null;
        Tasks.cancelAllTasks(this);
    }

}
