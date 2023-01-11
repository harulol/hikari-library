package dev.hawu.plugins.hikarilibrary;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.gui.GuiModel;
import dev.hawu.plugins.api.chat.ChatPacketAdapter;
import dev.hawu.plugins.api.collections.CooldownMap;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.commands.CommandRegistry;
import dev.hawu.plugins.api.events.Events;
import dev.hawu.plugins.api.gui.GuiClickEvents;
import dev.hawu.plugins.api.i18n.LanguageModule;
import dev.hawu.plugins.api.i18n.Locale;
import dev.hawu.plugins.api.impl.ChatPacketAdapterImpl;
import dev.hawu.plugins.api.impl.ParticlePacketAdapterImpl;
import dev.hawu.plugins.api.impl.TitlePacketAdapterImpl;
import dev.hawu.plugins.api.inventories.Inventories;
import dev.hawu.plugins.api.items.BukkitMaterial;
import dev.hawu.plugins.api.misc.PluginAdapter;
import dev.hawu.plugins.api.misc.WorldEntitiesLookupAdapter;
import dev.hawu.plugins.api.particles.ParticlePacketAdapter;
import dev.hawu.plugins.api.reflect.MinecraftVersion;
import dev.hawu.plugins.api.reflect.SimpleLookup;
import dev.hawu.plugins.api.title.TitlePacketAdapter;
import dev.hawu.plugins.hikarilibrary.commands.BaseCommand;
import dev.hawu.plugins.hikarilibrary.commands.LocaleCommand;
import dev.hawu.plugins.hikarilibrary.impl.UserAdapterImpl;
import dev.hawu.plugins.hikarilibrary.impl.WorldEntitiesLookupAdapterImpl;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class HikariLibrary extends JavaPlugin implements Listener {

    private static HikariLibrary instance;
    private LanguageModule module;
    private final CooldownMap<GuiModel> modelCooldownMap = new CooldownMap<>(500);

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

    @EventHandler
    private void onClick(final InventoryClickEvent event) {
        if(event.getInventory().getHolder() instanceof GuiModel) {
            final GuiModel model = (GuiModel) event.getInventory().getHolder();
            if(modelCooldownMap.isOnCooldown(model)) {
                event.setCancelled(true);
                return;
            }

            ((GuiModel) event.getInventory().getHolder()).handleClick(event);
            modelCooldownMap.putOnCooldown(model);
        }
    }

    @EventHandler
    private void onDrag(final InventoryDragEvent event) {
        if(event.getInventory().getHolder() instanceof GuiModel)
            ((GuiModel) event.getInventory().getHolder()).handleDrag(event);
    }

    @EventHandler
    private void onClose(final @NotNull InventoryCloseEvent event) {
        if(event.getInventory().getHolder() instanceof GuiModel)
            ((GuiModel) event.getInventory().getHolder()).handleClose();
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

        try {
            getLogger().info("Loading adapters for Minecraft " + MinecraftVersion.getCurrent().name());

            TitlePacketAdapter.setAdapter(TitlePacketAdapterImpl.getInstance());
            ParticlePacketAdapter.setAdapter(ParticlePacketAdapterImpl.getInstance());
            ChatPacketAdapter.setAdapter(ChatPacketAdapterImpl.INSTANCE);
            WorldEntitiesLookupAdapter.setAdapter(WorldEntitiesLookupAdapterImpl.getInstance());
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
            Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().getHolder() instanceof GuiModel)
                .forEach(Player::closeInventory);

            Bukkit.getOnlinePlayers().forEach(GuiClickEvents::safelyClose);
            Tasks.cancelAllTasks(this);
        } catch(final NoClassDefFoundError error) {
            error.printStackTrace();
            this.getLogger().severe("An error occurred. Did you /reload or hot-swap?");
        }
    }

}
