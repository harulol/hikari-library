package dev.hawu.plugins.api.gui.pagination;

import dev.hawu.plugins.api.Tasks;
import dev.hawu.plugins.api.adapters.PluginAdapter;
import dev.hawu.plugins.api.adapters.UserAdapter;
import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.events.Events;
import dev.hawu.plugins.api.gui.GuiClickEvents;
import dev.hawu.plugins.api.gui.GuiComponent;
import dev.hawu.plugins.api.gui.GuiModel;
import dev.hawu.plugins.api.i18n.Locale;
import dev.hawu.plugins.api.items.ItemCreator;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import dev.hawu.plugins.api.user.ExtendedUser;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a simple paginator to paginate
 * a list of elements.
 * <p>
 * The resulting gui models should be similar to
 * a doubly linked list.
 *
 * @param <T> The type of the elements in the paginating list.
 * @since 1.2
 */
public final class GuiPaginator<T> {

    private final List<GuiModel> models;

    private final PaginationData<T> data;
    private final PaginationDefaultData defaultData;
    private PaginationOptions<T> options;
    private PaginationControlOptions controlOptions;
    private PaginationFilterOptions filterOptions;

    /**
     * Initializes the data for the paginator to paginate.
     *
     * @param data           The necessary data.
     * @param options        The options for the paginator.
     * @param controlOptions The options for the control buttons.
     * @param filterOptions  The options for the filter.
     * @since 1.2
     */
    public GuiPaginator(final @NotNull PaginationData<T> data, final @NotNull PaginationOptions<T> options,
                        final @NotNull PaginationControlOptions controlOptions, final @NotNull PaginationFilterOptions filterOptions,
                        final @NotNull PaginationDefaultData defaultData) {
        this.models = new ArrayList<>();
        this.data = data;
        this.options = options;
        this.controlOptions = controlOptions;
        this.filterOptions = filterOptions;
        this.defaultData = defaultData;
    }

    /**
     * Constructs a new builder to build a pagination.
     *
     * @param <T> The type of the data.
     * @return Said new builder.
     * @since 1.2
     */
    @NotNull
    public static <T> GuiPaginationBuilder<T> newBuilder() {
        return new GuiPaginationBuilder<>();
    }

    /**
     * Builds and opens the first page to a human entity.
     *
     * @param entity The human entity to open the gui to.
     * @since 1.2
     */
    public void build(final @NotNull HumanEntity entity) {
        build(entity, 0, null);
    }

    /**
     * Builds and opens the page with the given index to a human entity.
     * <p>
     * This fails silently if page is invalid, and opens the first page
     * instead.
     *
     * @param entity The human entity to open the gui to.
     * @param page   The index of the page to open.
     * @since 1.2
     */
    public void build(final @NotNull HumanEntity entity, final int page) {
        build(entity, page, null);
    }

    private void finish(final @NotNull GuiModel model, final @Nullable String filter) {
        // Put the button to go "back".
        if(options.getBackAction() != null && options.getBackSlots().size() > 0 && options.getBackTemplate() != null) {
            final GuiComponent<?> backButton = new GuiComponent<Object>() {
                @Override
                public void handleClick(final @NotNull InventoryClickEvent event) {
                    event.setCancelled(true);
                    options.getBackAction().accept(event);
                }

                @Override
                @NotNull
                public ItemStack render() {
                    return options.getBackTemplate();
                }
            };
            options.getBackSlots().forEach(i -> model.mount(i, backButton));
        }

        // Put the button to filter elements if specified.
        if(filterOptions.getFilterTemplate() != null && filterOptions.getFilterSlots().size() > 0 && options.getPredicate() != null) {
            final GuiComponent<?> filterButton = new GuiComponent<Object>() {
                @Override
                public void handleClick(final @NotNull InventoryClickEvent event) {
                    event.setCancelled(true);
                    if(event.isRightClick()) {
                        build(event.getWhoClicked(), 0, null);
                        return;
                    }

                    event.getWhoClicked().closeInventory();
                    if(filterOptions.getFilterEvent() != null) filterOptions.getFilterEvent().accept(event);
                    GuiClickEvents.requestTextInput((Player) event.getWhoClicked(), s -> build(event.getWhoClicked(), 0, s));
                }

                @Override
                @NotNull
                public ItemStack render() {
                    return ItemStackBuilder.from(filterOptions.getFilterTemplate())
                        .replaceText(Pair.of("filter", filter == null ? "&cN/A" : "&a" + filter))
                        .build();
                }
            };
            filterOptions.getFilterSlots().forEach(i -> model.mount(i, filterButton));
        }

        // Last touches
        if(options.getLastTouches() != null) options.getLastTouches().accept(model);
    }

    private boolean hasNextPage(final int pageIndex) {
        return models.size() > 1 && pageIndex < models.size() - 1;
    }

    private boolean hasPreviousPage(final int pageIndex) {
        return models.size() > 1 && pageIndex > 0;
    }

    private GuiComponent<?> generateNextButton(final int currentIndex) {
        return new GuiComponent<Object>() {
            @Override
            public void handleClick(final @NotNull InventoryClickEvent event) {
                event.setCancelled(true);
                models.get(currentIndex + 1).open(event.getWhoClicked());
            }

            @Override
            @NotNull
            public ItemStack render() {
                return ItemStackBuilder.from(controlOptions.getNextButtonTemplate())
                    .replaceText(Pair.of("next", currentIndex + 2))
                    .build();
            }
        };
    }

    private GuiComponent<?> generatePreviousButton(final int currentIndex) {
        return new GuiComponent<Object>() {
            @Override
            public void handleClick(final @NotNull InventoryClickEvent event) {
                event.setCancelled(true);
                models.get(currentIndex - 1).open(event.getWhoClicked());
            }

            @Override
            @NotNull
            public ItemStack render() {
                return ItemStackBuilder.from(controlOptions.getPreviousButtonTemplate())
                    .replaceText(Pair.of("prev", currentIndex))
                    .build();
            }
        };
    }

    private void createControls() {
        // Usually this function is called after building and putting elements,
        // this function will loop once more and put navigation buttons to allow
        // users to move back and forth in the pagination.
        for(int i = 0; i < models.size(); i++) {
            final GuiModel page = models.get(i);
            final int curr = i;
            if(hasNextPage(i))
                controlOptions.getNextButtonSlots().forEach(slot -> page.mount(slot, generateNextButton(curr)));
            if(hasPreviousPage(i))
                controlOptions.getPreviousButtonSlots().forEach(slot -> page.mount(slot, generatePreviousButton(curr)));
        }
    }

    private void applyControlTranslations(final Locale locale) {
        final ItemStack previousTemplate = this.defaultData.isPreviousButtonTemplateChanged()
            ? controlOptions.getPreviousButtonTemplate()
            : ItemCreator.getPreviousButton(locale);
        final ItemStack nextTemplate = this.defaultData.isNextButtonTemplateChanged()
            ? controlOptions.getNextButtonTemplate()
            : ItemCreator.getNextButton(locale);

        this.controlOptions = new PaginationControlOptions(
            previousTemplate, controlOptions.getPreviousButtonSlots(),
            nextTemplate, controlOptions.getNextButtonSlots()
        );
    }

    private void applyFilterTranslations(final Locale locale) {
        final ItemStack filterTemplate = this.defaultData.isFilterTemplateChanged()
            ? filterOptions.getFilterTemplate()
            : ItemCreator.getFilterButton(locale);
        this.filterOptions = new PaginationFilterOptions(filterTemplate, filterOptions.getFilterSlots(), filterOptions.getFilterEvent());
    }

    private void applyOptionTranslations(final Locale locale) {
        final ItemStack backTemplate = this.defaultData.isBackTemplateChanged()
            ? options.getBackTemplate()
            : ItemCreator.getBackButton(locale);
        this.options = new PaginationOptions<>(backTemplate, options.getBackAction(), options.getBackSlots(), options.getPredicate(), options.getLastTouches());
    }

    /**
     * Builds the GUI and opens it to the specified player.
     *
     * @param entity    The player to open the GUI to
     * @param knownPage The page to open.
     * @param filter    The filter term to apply to the collection.
     * @since 1.2
     */
    public void build(final @NotNull HumanEntity entity, final int knownPage, final @Nullable String filter) {
        if(entity instanceof Player) {
            final ExtendedUser user = UserAdapter.getAdapter().getUser(entity.getUniqueId());
            applyControlTranslations(user.getLocale());
            applyFilterTranslations(user.getLocale());
            applyOptionTranslations(user.getLocale());
        }

        models.clear();
        GuiModel current = data.getModelSupplier().get();
        current.setUnmountOnClose(false);
        int currentPage = 0;
        int pointer = 0;

        for(final T element : options.getFilteredItems(data.getCollection(), filter)) {
            if(pointer >= data.getAllowedSlots().size()) {
                finish(current, filter);
                models.add(current);
                currentPage++;
                current = data.getModelSupplier().get();
                current.setUnmountOnClose(false);
                pointer = 0;
            }

            final int page = currentPage;
            current.mount(data.getAllowedSlots().get(pointer), data.getItemGenerator().apply(element, page));
            pointer++;
        }

        finish(current, filter);
        models.add(current);
        createControls();

        if(knownPage >= 0 && knownPage < models.size()) models.get(knownPage).open(entity);
        else models.get(0).open(entity);

        scheduleUnmount(entity.getUniqueId());
    }

    // Since we disable "unmountOnClose" for all pagination system.
    // We need to schedule some sort of events to unmount everything
    // once the player has closed out of the pagination.
    // Close buttons are not reliable as they can use ESC, or E.
    private void scheduleUnmount(final UUID uuid) {
        final Listener listener = new Listener() {
            BukkitTask task;

            private void unmountAll() {
                models.forEach(model -> {
                    for(int i = 0; i < model.getInventory().getSize(); i++)
                        model.unmount(i);
                });
            }

            @EventHandler
            public void onClose(final InventoryCloseEvent event) {
                if(!event.getPlayer().getUniqueId().equals(uuid)) return;

                // A task to unmount all if the player does not have another page open in 5 ticks.
                // Idk just to account for lag?
                task = Tasks.run(this::unmountAll).delay(5).async(true).run();
            }

            @EventHandler
            public void onQuit(final PlayerQuitEvent event) {
                if(!event.getPlayer().getUniqueId().equals(uuid)) return;
                if(task != null) task.cancel();
                Tasks.run(this::unmountAll).async(true).run();
            }

            @EventHandler
            public void onOpen(final InventoryOpenEvent event) {
                if(!event.getPlayer().getUniqueId().equals(uuid)) return;
                if(!(event.getInventory().getHolder() instanceof GuiModel)) return;

                final GuiModel model = (GuiModel) event.getInventory().getHolder();
                if(models.contains(model) && task != null)
                    task.cancel();
            }
        };

        Events.registerEvents(PluginAdapter.getPlugin(), listener);
    }

}
