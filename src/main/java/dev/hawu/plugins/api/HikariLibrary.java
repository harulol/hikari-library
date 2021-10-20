package dev.hawu.plugins.api;

import dev.hawu.plugins.api.inventories.ClickEvents;
import dev.hawu.plugins.api.inventories.item.BukkitMaterial;
import org.bukkit.plugin.java.JavaPlugin;

public final class HikariLibrary extends JavaPlugin {

    @Override
    public void onEnable() {
        ClickEvents.init(this);
        BukkitMaterial.reInitDisplayableMaterials(this);
    }

}
