package dev.hawu.plugins.api.gui.pagination;

import dev.hawu.plugins.api.gui.GuiElement;
import dev.hawu.plugins.api.gui.GuiModel;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * A builder to build an instance of {@link GuiPaginator}.
 *
 * @param <T> The type of the data in the collection.
 * @since 1.2
 */
public final class GuiPaginationBuilder<T> {

    private Collection<T> collection;
    private Supplier<GuiModel> modelSupplier;
    private List<Integer> allowedSlots;
    private BiFunction<T, Integer, GuiElement<?>> itemGenerator;

    private ItemStack backTemplate;
    private Consumer<InventoryClickEvent> backAction;
    private Set<Integer> backSlots;

    private BiPredicate<T, String> predicate;
    private Consumer<GuiModel> lastTouches;

    private ItemStack previousButtonTemplate;
    private Set<Integer> previousButtonSlots;
    private ItemStack nextButtonTemplate;
    private Set<Integer> nextButtonSlots;

    private ItemStack filterTemplate;
    private Set<Integer> filterSlots;
    private Consumer<InventoryClickEvent> filterEvent;

    {
        // Create a default value for modelSupplier.
        modelSupplier = () -> {
            final GuiModel model = new GuiModel(54);

            model.mount(49, new GuiElement<Void>() {
                @Override
                public void handleClick(final @NotNull InventoryClickEvent event) {
                    event.setCancelled(true);
                    event.getWhoClicked().closeInventory();
                }

                @Override
                public @NotNull ItemStack render() {
                    return ItemStackBuilder.of(Material.BARRIER)
                        .name("&cClose")
                        .lore("&7Close this menu")
                        .build();
                }
            });
            return model;
        };

        // Create default value for allowedSlots.
        allowedSlots = new ArrayList<>();
        IntStream.range(0, 54)
            .filter(i -> (i % 9 != 0 && i % 9 != 8) && i > 8 && i < 45)
            .forEach(allowedSlots::add);

        // Create default value for backTemplate.
        backTemplate = ItemStackBuilder.of(Material.SIGN)
            .name("&aGo Back")
            .lore("&7Go back to the previous menu.")
            .build();

        // Create default value for backSlots.
        backSlots = Collections.singleton(48);

        // Create default value for predicate.
        predicate = (item, filter) -> true;

        // Create default value for lastTouches.
        lastTouches = model -> {};

        // Create default value for the previous button template.
        previousButtonTemplate = ItemStackBuilder.of(Material.ARROW)
            .name("&aPrevious Page")
            .lore("&7Go back to page %prev%.")
            .build();

        // Create default value for the previous button slots.
        previousButtonSlots = new HashSet<>(Arrays.asList(18, 27));

        // Create default value for the next button template.
        nextButtonTemplate = ItemStackBuilder.of(Material.ARROW)
            .name("&aNext Page")
            .lore("&7Go to page %next%.")
            .build();

        // Create default value for the next button slots.
        nextButtonSlots = new HashSet<>(Arrays.asList(26, 35));

        // Create default value for the filter template.
        filterTemplate = ItemStackBuilder.of(Material.HOPPER)
            .name("&eFilter")
            .lore(
                "&7Returns only the elements",
                "&7that matches the given",
                "&7filter term.",
                "",
                "&fCurrent Filter:",
                "%filter%",
                "",
                "&6Click &eto change.",
                "&6Right click &eto clear."
            )
            .build();

        // Create default value for the filter slots.
        filterSlots = Collections.singleton(50);
    }

    /**
     * Sets the collection to be used for paginating.
     *
     * @param collection The collection to use.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setCollection(final @NotNull Collection<@NotNull T> collection) {
        this.collection = collection;
        return this;
    }

    /**
     * Sets the supplier that provides GUI models as
     * templates for each page.
     * <p>
     * The default supplier supplies an empty model
     * with 54 slots (a double chest inventory) and
     * a close button situated at slot indexed 49.
     *
     * @param modelSupplier The supplier.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setModelSupplier(final @NotNull Supplier<@NotNull GuiModel> modelSupplier) {
        this.modelSupplier = modelSupplier;
        return this;
    }

    /**
     * Sets the slots to generate elements for and mount on
     * in each page.
     * <p>
     * The default value is the inner slots of a 54-slot
     * inventory.
     *
     * @param allowedSlots Said slots.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setAllowedSlots(final @NotNull List<@NotNull Integer> allowedSlots) {
        this.allowedSlots = allowedSlots;
        return this;
    }

    /**
     * Sets the function to generate an element for each item
     * in the paginating collection.
     * <p>
     * Said function takes in a type T, which is the data type
     * of the collection, and the page index of the model where
     * the element is mounted on.
     *
     * @param itemGenerator The function.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setItemGenerator(final @NotNull BiFunction<@NotNull T, @NotNull Integer, @Nullable GuiElement<?>> itemGenerator) {
        this.itemGenerator = itemGenerator;
        return this;
    }

    /**
     * Sets the template for the back button.
     * <p>
     * The default value is a sign saying "Go Back" in green.
     *
     * @param backTemplate The template.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setBackTemplate(final @Nullable ItemStack backTemplate) {
        this.backTemplate = backTemplate;
        return this;
    }

    /**
     * Sets the action to be executed when the back button is clicked.
     *
     * @param backAction The action.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setBackAction(final @Nullable Consumer<@NotNull InventoryClickEvent> backAction) {
        this.backAction = backAction;
        return this;
    }

    /**
     * Sets the slots to mount the back button on.
     * <p>
     * The default value is a singleton set
     * of just 48.
     *
     * @param backSlots The slots.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setBackSlots(final @NotNull Set<@NotNull Integer> backSlots) {
        this.backSlots = backSlots;
        return this;
    }

    /**
     * Sets the predicate to be used for testing whether an item
     * can show up with a filter term applied.
     * <p>
     * The default value is a function that always resolves
     * to true, no matter the input.
     *
     * @param predicate The predicate.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setPredicate(final @Nullable BiPredicate<@NotNull T, @NotNull String> predicate) {
        this.predicate = predicate;
        return this;
    }

    /**
     * Sets the function to be executed after a page has been finished,
     * and just requires final modifications.
     * <p>
     * The default value is just an empty consumer, doing
     * nothing.
     *
     * @param lastTouches The function.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setLastTouches(final @Nullable Consumer<@NotNull GuiModel> lastTouches) {
        this.lastTouches = lastTouches;
        return this;
    }

    /**
     * Sets the template to use for the button that traverses
     * the pagination backwards.
     * <p>
     * The default value is an arrow that says "Previous Page" in green
     * and tell what page it is going to in lore.
     *
     * @param previousButtonTemplate The template.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setPreviousButtonTemplate(final @NotNull ItemStack previousButtonTemplate) {
        this.previousButtonTemplate = previousButtonTemplate;
        return this;
    }

    /**
     * Sets the slots to mount previous buttons on.
     * <p>
     * The default value is the 2 slots indexed 18, 27.
     *
     * @param previousButtonSlots The slots.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setPreviousButtonSlots(final @NotNull Set<@NotNull Integer> previousButtonSlots) {
        this.previousButtonSlots = previousButtonSlots;
        return this;
    }

    /**
     * Sets the template to use for the button that traverses
     * the pagination forwards.
     * <p>
     * The default value is an arrow that says "Next Page" in green
     * and tell what page it is going to in lore.
     *
     * @param nextButtonTemplate The template.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setNextButtonTemplate(final @NotNull ItemStack nextButtonTemplate) {
        this.nextButtonTemplate = nextButtonTemplate;
        return this;
    }

    /**
     * Sets the slots to mount next buttons on.
     * <p>
     * The default value is the 2 slots indexed 26, 35.
     *
     * @param nextButtonSlots The slots.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setNextButtonSlots(final @NotNull Set<@NotNull Integer> nextButtonSlots) {
        this.nextButtonSlots = nextButtonSlots;
        return this;
    }

    /**
     * Sets the template to use for the button that
     * applies a filter to the pagination.
     * <p>
     * The default value is a hopper with the name "Filter"
     * in yellow, which also shows the current filter
     * in its lore.
     *
     * @param filterTemplate The template.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setFilterTemplate(final @Nullable ItemStack filterTemplate) {
        this.filterTemplate = filterTemplate;
        return this;
    }

    /**
     * Sets the slots to mount filter buttons on.
     * <p>
     * The default value is just the slot 50, right next
     * to the close button.
     *
     * @param filterSlots The slots.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setFilterSlots(final @NotNull Set<@NotNull Integer> filterSlots) {
        this.filterSlots = filterSlots;
        return this;
    }

    /**
     * Sets the function to be triggered when the filter buttons
     * are clicked and before the invocation to {@link dev.hawu.plugins.api.gui.GuiClickEvents#requestTextInput(Player, Consumer)}.
     *
     * @param filterEvent The function.
     * @return This builder.
     * @since 1.2
     */
    @NotNull
    public GuiPaginationBuilder<T> setFilterEvent(final @Nullable Consumer<@NotNull InventoryClickEvent> filterEvent) {
        this.filterEvent = filterEvent;
        return this;
    }

    /**
     * Constructs the paginator after building.
     *
     * @return The paginator.
     * @since 1.2
     */
    @NotNull
    public GuiPaginator<T> build() {
        Objects.requireNonNull(collection, "Paginating collection cannot be null.");
        Objects.requireNonNull(itemGenerator, "The item generator cannot be null.");

        final PaginationData<T> data = new PaginationData<>(collection, modelSupplier, allowedSlots, itemGenerator);
        final PaginationOptions<T> options = new PaginationOptions<>(backTemplate, backAction, backSlots, predicate, lastTouches);
        final PaginationControlOptions controlOptions = new PaginationControlOptions(previousButtonTemplate, previousButtonSlots,
            nextButtonTemplate, nextButtonSlots);
        final PaginationFilterOptions filterOptions = new PaginationFilterOptions(filterTemplate, filterSlots, filterEvent);
        return new GuiPaginator<>(data, options, controlOptions, filterOptions);
    }

    /**
     * Constructs the paginator then immediately builds
     * and opens it to a human entity.
     * <p>
     * Shorthand for {@code build().build(entity)}.
     *
     * @param entity The entity to open to.
     * @since 1.2
     */
    public void build(final @NotNull HumanEntity entity) {
        build().build(entity);
    }

    /**
     * Constructs the paginator then immediately builds
     * and opens it to a human entity.
     * <p>
     * Shorthand for {@code build().build(entity, page)}.
     *
     * @param entity The entity to open to.
     * @param page   The page to open.
     * @since 1.2
     */
    public void build(final @NotNull HumanEntity entity, final int page) {
        build().build(entity, page);
    }

    /**
     * Constructs the paginator then immediately builds
     * and opens it to a human entity.
     * <p>
     * Shorthand for {@code build().build(entity, page, filter)}.
     *
     * @param entity The entity to open to.
     * @param page   The page to open.
     * @param filter The filter to apply.
     * @since 1.2
     */
    public void build(final @NotNull HumanEntity entity, final int page, final @Nullable String filter) {
        build().build(entity, page, filter);
    }

}
