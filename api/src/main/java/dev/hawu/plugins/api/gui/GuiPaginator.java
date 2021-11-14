package dev.hawu.plugins.api.gui;

import dev.hawu.plugins.api.collections.tuples.Pair;
import dev.hawu.plugins.api.items.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

/**
 * Dead simple representation of a list of
 * gui models.
 *
 * @since 1.2
 */
public final class GuiPaginator {

    private final List<GuiModel> models = new ArrayList<>();
    private final Set<Integer> previousSlots = new HashSet<>();
    private final Set<Integer> nextSlots = new HashSet<>();

    // Items to be used as template for the next and previous buttons.
    // Lore lines with %prev% and %next% will be replaced accordingly.
    private ItemStack next = ItemStackBuilder.of(Material.ARROW)
        .name("&aPrevious Page")
        .lore("&7Go to page %prev%")
        .build();
    private ItemStack previous = ItemStackBuilder.of(Material.ARROW)
        .name("&aNext Page")
        .lore("&7Go to page %next%")
        .build();

    /**
     * Adds a model to the underlying list.
     *
     * @param model The model to add.
     * @since 1.2
     */
    public void append(@NotNull final GuiModel model) {
        models.add(model);
    }

    /**
     * Adds a model to the underlying list at the position provided.
     *
     * @param index The position to insert.
     * @param model The model to add.
     * @since 1.2
     */
    public void append(final int index, @NotNull final GuiModel model) {
        models.add(index, model);
    }

    /**
     * Adds all the provided integers to be slots to place
     * buttons that traverse the list backwards.
     *
     * @param indicies The slots to place previous buttons.
     * @since 1.2
     */
    public void addAllPreviousSlots(final int @NotNull ... indicies) {
        Arrays.stream(indicies).forEach(previousSlots::add);
    }

    /**
     * Adds all the provided integers to be slots to place
     * buttons that traverse the list forwards.
     *
     * @param indices The slots to place next buttons.
     * @since 1.2
     */
    public void addAllNextSlots(final int @NotNull ... indices) {
        Arrays.stream(indices).forEach(nextSlots::add);
    }

    /**
     * Sets the template for the next button. The tag {@code %next%} in its lore
     * will be replaced with the next page accordingly in {@link GuiPaginator#createControls()}.
     *
     * @param item The template item.
     * @since 1.2
     */
    public void setNext(@NotNull final ItemStack item) {
        next = item;
    }

    /**
     * Sets the template for the previous button. The tag {@code %prev%} in its lore
     * will be replaced with the previous page accordingly in {@link GuiPaginator#createControls()}.
     *
     * @param item The template item.
     * @since 1.2
     */
    public void setPrevious(@NotNull final ItemStack item) {
        previous = item;
    }

    private boolean hasNext(int index) {
        return models.size() > 1 && index != models.size() - 1;
    }

    private boolean hasPrevious(int index) {
        return models.size() > 1 && index != 0;
    }

    /**
     * Runs through the underlying list, and places buttons for players
     * to traverse the pagination.
     * <p>
     * First page will not have buttons to traverse backwards, and last page
     * will not have buttons to traverse forwards.
     *
     * @since 1.2
     */
    public void createControls() {
        for(int i = 0; i < models.size(); i++) {
            final GuiModel model = models.get(i);
            if(hasNext(i)) {
                final ItemStack nextButton = ItemStackBuilder.from(next)
                    .replaceText(Pair.of("next", i + 2))
                    .build();

                final int current = i;
                final Supplier<GuiElement<?>> elementSupplier = () -> new GuiElement<Object>() {
                    @Override
                    public void handleClick(final @NotNull InventoryClickEvent event) {
                        event.setCancelled(true);
                        get(current + 1).open(event.getWhoClicked());
                    }

                    @Override
                    public @NotNull ItemStack render() {
                        return nextButton;
                    }
                };

                nextSlots.forEach(j -> model.mount(j, elementSupplier.get()));
            }

            if(hasPrevious(i)) {
                final ItemStack prevButton = ItemStackBuilder.from(previous)
                    .replaceText(Pair.of("prev", i))
                    .build();

                final int current = i;
                final Supplier<GuiElement<?>> elementSupplier = () -> new GuiElement<Object>() {
                    @Override
                    public void handleClick(final @NotNull InventoryClickEvent event) {
                        event.setCancelled(true);
                        get(current - 1).open(event.getWhoClicked());
                    }

                    @Override
                    public @NotNull ItemStack render() {
                        return prevButton;
                    }
                };

                previousSlots.forEach(j -> model.mount(j, elementSupplier.get()));
            }
        }
    }

    /**
     * Gets the model at the provided position.
     *
     * @param index The position to retrieve at.
     * @return The widget at the position, or throw an error.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     * @since 1.0
     */
    @NotNull
    public GuiModel get(final int index) {
        return models.get(index);
    }

    /**
     * Opens the pagination to a human entity. This attempts to open
     * the page at the position provided first, and only if this fails
     * will it open the first page to the entity.
     *
     * @param entity The entity to open to.
     * @param page   The page to open.
     * @since 1.0
     */
    public void open(@NotNull final HumanEntity entity, final int page) {
        if(page >= models.size()) models.get(0).open(entity);
        else models.get(page).open(entity);
    }

}
