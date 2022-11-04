package dev.hawu.plugins.api.integrations;

import dev.hawu.plugins.api.misc.ConsoleLogger;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * The short integration util for accessing
 * Vault API without having to do too much.
 *
 * @since 1.7
 */
public final class VaultAPI {

    private static VaultAPI instance;
    private Economy economy;
    private Permission permission;
    private Chat chat;

    private VaultAPI() {
        // Intentionally left blank.
    }

    /**
     * Gets the instance of the VaultAPI.
     *
     * @return The instance of the VaultAPI.
     * @since 1.7
     */
    public static VaultAPI getInstance() {
        if(instance != null) return instance;
        else return instance = new VaultAPI();
    }

    /**
     * Attempts to hook onto the Vault plugin.
     *
     * @return Whether the hook was successful.
     * @since 1.7
     */
    public boolean hook(final boolean force) {
        if(Bukkit.getPluginManager().getPlugin("Vault") == null) {
            ConsoleLogger.severe("Vault was not found.");
            return false;
        }

        if(economy == null || force)
            setupEconomy();

        if(permission == null || force)
            setupPermission();

        if(chat == null || force)
            setupChat();
        return true;
    }

    /**
     * Retrieves the supposedly hooked economy instance.
     *
     * @return The economy instance.
     * @since 1.7
     */
    public Economy getEconomy() {
        return economy;
    }

    /**
     * Retrieves the supposedly hooked permission instance.
     *
     * @return The permission instance.
     * @since 1.7
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Retrieves the supposedly hooked chat instance.
     *
     * @return The chat instance.
     * @since 1.7
     */
    public Chat getChat() {
        return chat;
    }

    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if(rsp != null)
            economy = rsp.getProvider();
        else ConsoleLogger.warn("Was unable to register economy.");
    }

    private void setupPermission() {
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager().getRegistration(Permission.class);
        if(rsp != null)
            permission = rsp.getProvider();
        else ConsoleLogger.warn("Was unable to register permissions.");
    }

    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServicesManager().getRegistration(Chat.class);
        if(rsp != null)
            chat = rsp.getProvider();
        else ConsoleLogger.warn("Was unable to register chat.");
    }

}
