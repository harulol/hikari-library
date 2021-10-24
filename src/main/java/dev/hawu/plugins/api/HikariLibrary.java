package dev.hawu.plugins.api;

import dev.hawu.plugins.api.commands.BaseCommand;
import dev.hawu.plugins.api.inventories.ClickEvents;
import dev.hawu.plugins.api.inventories.item.BukkitMaterial;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public final class HikariLibrary extends JavaPlugin {

    private static HikariLibrary instance;
    private static String authToken;

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
        this.saveDefaultConfig();
        authToken = this.getConfig().getString("authToken");
        if(authToken.matches("[^A-Za-z0-9]+")) {
            getLogger().severe("Authentication token is invalid.");
            authToken = null;
        }
    }

}
