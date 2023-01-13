package dev.hawu.plugins.hikarilibrary;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.adapters.*;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.commands.CommandRegistry;
import dev.hawu.plugins.api.events.Events;
import dev.hawu.plugins.api.gui.GuiClickEvents;
import dev.hawu.plugins.api.i18n.LanguageModule;
import dev.hawu.plugins.api.i18n.Locale;
import dev.hawu.plugins.api.impl.TitlePacketAdapterImpl;
import dev.hawu.plugins.api.inventories.Inventories;
import dev.hawu.plugins.api.items.BukkitMaterial;
import dev.hawu.plugins.api.misc.ConsoleLogger;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import dev.hawu.plugins.api.reflect.SimpleLookup;
import dev.hawu.plugins.hikarilibrary.commands.BaseCommand;
import dev.hawu.plugins.hikarilibrary.commands.LocaleCommand;
import dev.hawu.plugins.hikarilibrary.impl.UserAdapterImpl;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HikariLibrary extends JavaPlugin implements Listener {

    private static HikariLibrary instance;
    private LanguageModule module;

    /**
     * Retrieves the instance of the library.
     *
     * @return the instance of the library
     * @since 1.6
     */
    public static HikariLibrary getInstance() {
        return instance;
    }

    /**
     * Gets the messages module.
     *
     * @return the messages module
     * @since 1.6
     */
    public static LanguageModule getModule() {
        return getInstance().module;
    }

    /**
     * Translates a message.
     *
     * @param key  the key of the message
     * @param args the arguments to replace in the message
     * @return the translated message
     * @since 1.6
     */
    public static String tl(final Locale locale, final String key, final Pair<?, ?>... args) {
        return getModule().translate(locale, key, args);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onLogin(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        UserAdapterImpl.addUser(player.getUniqueId());
    }

    private void tryLoading(final String name, final Runnable runnable) {
        try {
            runnable.run();
        } catch(final Throwable t) {
            t.printStackTrace();
            ConsoleLogger.severef("Couldn't load %s: %s", name, t.getMessage());
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        module = new LanguageModule(this, "messages");

        BukkitMaterial.initialize();
        GuiClickEvents.initialize(this);
        Inventories.setPlugin(this);
        Events.registerEvents(this, this);
        PluginAdapter.setPlugin(this);
        ConfigurationSerialization.registerClass(CraftUser.class);
        UserAdapterImpl.init(this);

        CommandRegistry.register(this, new BaseCommand(this), new LocaleCommand());

        // Attempt to load necessary registries and adapters.
        ConsoleLogger.logf("Loading adapters for %s", MinecraftVersion.getCurrent().name());
        tryLoading("ChatPacketAdapter", ChatPacketAdapter::getAdapter);
        tryLoading("ParticlePacketAdapter", ParticlePacketAdapter::getAdapter);
        tryLoading("WorldEntitiesLookupAdapter", WorldEntitiesLookupAdapter::getAdapter);

        try {
            TitlePacketAdapter.setAdapter(TitlePacketAdapterImpl.getInstance());
        } catch (final Exception e) {
            e.printStackTrace();
            getLogger().severe("Failed to load adapters for Minecraft " + SimpleLookup.getBukkitVersion());
        }
    }

    @Override
    public void onDisable() {
        UserAdapterImpl.save();
        GuiClickEvents.onDisable();
        HandlerList.unregisterAll((Plugin) this);
        CommandRegistry.unregisterAll();

        try {
            Bukkit.getOnlinePlayers().forEach(GuiClickEvents::safelyClose);
            Tasks.cancelAllTasks(this);
        } catch(final NoClassDefFoundError error) {
            error.printStackTrace();
            this.getLogger().severe("An error occurred. Did you /reload or hot-swap?");
        }
    }

}
